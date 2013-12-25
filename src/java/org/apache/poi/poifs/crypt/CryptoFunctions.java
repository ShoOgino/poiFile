/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.poifs.crypt;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianConsts;

/**
 * Helper functions used for standard and agile encryption
 */
public class CryptoFunctions {
    /**
     * 2.3.4.7 ECMA-376 Document Encryption Key Generation (Standard Encryption)
     * 2.3.4.11 Encryption Key Generation (Agile Encryption)
     * 
     * The encryption key for ECMA-376 document encryption [ECMA-376] using agile encryption MUST be 
     * generated by using the following method, which is derived from PKCS #5: Password-Based
     * Cryptography Version 2.0 [RFC2898].
     * 
     * Let H() be a hashing algorithm as determined by the PasswordKeyEncryptor.hashAlgorithm
     * element, H_n be the hash data of the n-th iteration, and a plus sign (+) represent concatenation. The
     * password MUST be provided as an array of Unicode characters. Limitations on the length of the
     * password and the characters used by the password are implementation-dependent. The initial
     * password hash is generated as follows:
     * 
     * - H_0 = H(salt + password)
     * 
     * The salt used MUST be generated randomly. The salt MUST be stored in the
     * PasswordKeyEncryptor.saltValue element contained within the \EncryptionInfo stream (1) as
     * specified in section 2.3.4.10. The hash is then iterated by using the following approach:
     * 
     * - H_n = H(iterator + H_n-1)
     * 
     * where iterator is an unsigned 32-bit value that is initially set to 0x00000000 and then incremented
     * monotonically on each iteration until PasswordKey.spinCount iterations have been performed.
     * The value of iterator on the last iteration MUST be one less than PasswordKey.spinCount.
     * 
     * For POI, H_final will be calculated by {@link generateKey()}
     *
     * @param password
     * @param hashAlgorithm
     * @param salt
     * @param spinCount
     * @return
     */
    public static byte[] hashPassword(String password, HashAlgorithm hashAlgorithm, byte salt[], int spinCount) {
        // If no password was given, use the default
        if (password == null) {
            password = Decryptor.DEFAULT_PASSWORD;
        }
        
        MessageDigest hashAlg = getMessageDigest(hashAlgorithm);
        
        hashAlg.update(salt);
        byte[] hash = hashAlg.digest(getUtf16LeString(password));
        byte[] iterator = new byte[LittleEndianConsts.INT_SIZE];
        
        try {
            for (int i = 0; i < spinCount; i++) {
                LittleEndian.putInt(iterator, 0, i);
                hashAlg.reset();
                hashAlg.update(iterator);
                hashAlg.update(hash);
                hashAlg.digest(hash, 0, hash.length); // don't create hash buffer everytime new
            }
        } catch (DigestException e) {
            throw new EncryptedDocumentException("error in password hashing");
        }
        
        return hash;
    }    

    /**
     * 2.3.4.12 Initialization Vector Generation (Agile Encryption)
     * 
     * Initialization vectors are used in all cases for agile encryption. An initialization vector MUST be
     * generated by using the following method, where H() is a hash function that MUST be the same as
     * specified in section 2.3.4.11 and a plus sign (+) represents concatenation:
     * 1. If a blockKey is provided, let IV be a hash of the KeySalt and the following value:
     *    blockKey: IV = H(KeySalt + blockKey)
     * 2. If a blockKey is not provided, let IV be equal to the following value:
     *    KeySalt:IV = KeySalt.
     * 3. If the number of bytes in the value of IV is less than the the value of the blockSize attribute
     *    corresponding to the cipherAlgorithm attribute, pad the array of bytes by appending 0x36 until
     *    the array is blockSize bytes. If the array of bytes is larger than blockSize bytes, truncate the
     *    array to blockSize bytes. 
     **/
    public static byte[] generateIv(HashAlgorithm hashAlgorithm, byte[] salt, byte[] blockKey, int blockSize) {
        byte iv[] = salt;
        if (blockKey != null) {
            MessageDigest hashAlgo = getMessageDigest(hashAlgorithm);
            hashAlgo.update(salt);
            iv = hashAlgo.digest(blockKey);
        }
        return getBlock36(iv, blockSize);
    }

    /**
     * 2.3.4.11 Encryption Key Generation (Agile Encryption)
     * 
     * ... continued ...
     * 
     * The final hash data that is used for an encryption key is then generated by using the following
     * method:
     * 
     * - H_final = H(H_n + blockKey)
     * 
     * where blockKey represents an array of bytes used to prevent two different blocks from encrypting
     * to the same cipher text.
     * 
     * If the size of the resulting H_final is smaller than that of PasswordKeyEncryptor.keyBits, the key
     * MUST be padded by appending bytes with a value of 0x36. If the hash value is larger in size than
     * PasswordKeyEncryptor.keyBits, the key is obtained by truncating the hash value. 
     *
     * @param passwordHash
     * @param hashAlgorithm
     * @param blockKey
     * @param keySize
     * @return
     */
    public static byte[] generateKey(byte[] passwordHash, HashAlgorithm hashAlgorithm, byte[] blockKey, int keySize) {
        MessageDigest hashAlgo = getMessageDigest(hashAlgorithm);
        hashAlgo.update(passwordHash);
        byte[] key = hashAlgo.digest(blockKey);
        return getBlock36(key, keySize);
    }

    public static Cipher getCipher(SecretKey key, CipherAlgorithm cipherAlgorithm, ChainingMode chain, byte[] vec, int cipherMode) {
        return getCipher(key, cipherAlgorithm, chain, vec, cipherMode, null);
    }

    /**
     * 
     *
     * @param key
     * @param chain
     * @param vec
     * @param cipherMode Cipher.DECRYPT_MODE or Cipher.ENCRYPT_MODE
     * @return
     * @throws GeneralSecurityException
     */
    public static Cipher getCipher(SecretKey key, CipherAlgorithm cipherAlgorithm, ChainingMode chain, byte[] vec, int cipherMode, String padding) {
        int keySizeInBytes = key.getEncoded().length;
        if (padding == null) padding = "NoPadding";
        
        try {
            // Ensure the JCE policies files allow for this sized key
            if (Cipher.getMaxAllowedKeyLength(key.getAlgorithm()) < keySizeInBytes*8) {
                throw new EncryptedDocumentException("Export Restrictions in place - please install JCE Unlimited Strength Jurisdiction Policy files");
            }

            Cipher cipher;
            if (cipherAlgorithm.needsBouncyCastle) {
                registerBouncyCastle();
                cipher = Cipher.getInstance(key.getAlgorithm() + "/" + chain.jceId + "/" + padding, "BC");
            } else {
                cipher = Cipher.getInstance(key.getAlgorithm() + "/" + chain.jceId + "/" + padding);
            }
            
            if (vec == null) {
                cipher.init(cipherMode, key);
            } else {
                AlgorithmParameterSpec aps;
                if (cipherAlgorithm == CipherAlgorithm.rc2) {
                    aps = new RC2ParameterSpec(key.getEncoded().length*8, vec);
                } else {
                    aps = new IvParameterSpec(vec);
                }
                cipher.init(cipherMode, key, aps);
            }
            return cipher;
        } catch (GeneralSecurityException e) {
            throw new EncryptedDocumentException(e);
        }
    }    
    
    public static byte[] getBlock36(byte[] hash, int size) {
        return getBlockX(hash, size, (byte)0x36);
    }

    public static byte[] getBlock0(byte[] hash, int size) {
        return getBlockX(hash, size, (byte)0);
    }
    
    private static byte[] getBlockX(byte[] hash, int size, byte fill) {
        if (hash.length == size) return hash;
        
        byte[] result = new byte[size];
        Arrays.fill(result, fill);
        System.arraycopy(hash, 0, result, 0, Math.min(result.length, hash.length));
        return result;
    }
    
    public static byte[] getUtf16LeString(String str) {
        try {
            return str.getBytes("UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            throw new EncryptedDocumentException(e);
        }
    }
    
    public static MessageDigest getMessageDigest(HashAlgorithm hashAlgorithm) {
        try {
            if (hashAlgorithm.needsBouncyCastle) {
                registerBouncyCastle();
                return MessageDigest.getInstance(hashAlgorithm.jceId, "BC");
            } else {
                return MessageDigest.getInstance(hashAlgorithm.jceId);
            }
        } catch (GeneralSecurityException e) {
            throw new EncryptedDocumentException("hash algo not supported", e);
        }
    }
    
    public static Mac getMac(HashAlgorithm hashAlgorithm) {
        try {
            if (hashAlgorithm.needsBouncyCastle) {
                registerBouncyCastle();
                return Mac.getInstance(hashAlgorithm.jceHmacId, "BC");
            } else {
                return Mac.getInstance(hashAlgorithm.jceHmacId);
            }
        } catch (GeneralSecurityException e) {
            throw new EncryptedDocumentException("hmac algo not supported", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void registerBouncyCastle() {
        if (Security.getProvider("BC") != null) return;
        try {
            Class<Provider> clazz = (Class<Provider>)Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
            Security.addProvider(clazz.newInstance());
        } catch (Exception e) {
            throw new EncryptedDocumentException("Only the BouncyCastle provider supports your encryption settings - please add it to the classpath.");
        }
    }
}
