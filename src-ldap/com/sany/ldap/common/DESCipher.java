/*
 * @(#)DESCipher.java
 * 
 * Copyright(c)2001-2008 SANY Heavy Industry Co.,Ltd
 * All right reserved.
 * 
 * 这个软件是属于三一重工股份有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一重工股份有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Heavy Industry Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Heavy Industry Co, Ltd.
 */
package com.sany.ldap.common;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;

/**
 * DESCipher.java
 * 
 * 一个采用DES算法的加密解密类,可以通过设置密钥或使用默认密钥，对字符串和字节数组进行加密和解密运算。
 * 
 * @author 周正
 * @company SANY Heavy Industry Co, Ltd
 * @creation date 2008/03/18
 * @version $Revision: 1629 $
 */
public class DESCipher {

    private static final String DEFAULTKEY = "SANY";

    private Cipher encryptCipher = null;

    private Cipher decryptCipher = null;

    /**
     * 将参数字节数组转换为16进制值表示组合而成的字符串。
     * 
     * @param byte[] 需要转换的byte数组
     * @return String 转换后的字符串
     * @throws Exception
     *             JAVA异常
     */
    public static String byteGrpToHexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        StringBuffer tempSB = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {
                tempSB.append("0");
            }
            tempSB.append(Integer.toString(intTmp, 16));
        }
        return tempSB.toString();
    }

    /**
     * 将参数16进制值表示组合而成的字符串转换为字节数组。
     * 
     * @param String
     *            需要转换的字符串
     * @return byte[] 转换后的byte数组
     * @throws Exception
     *             JAVA异常
     */
    public static byte[] hexStrToByteGrp(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 默认构造方法，使用默认密钥
     * 
     * @throws Exception
     */
    public DESCipher() throws Exception {
        this(DEFAULTKEY);
    }

    /**
     * 指定密钥构造方法
     * 
     * @param String
     *            指定的密钥
     * @throws Exception
     *             JAVA异常
     */
    public DESCipher(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 加密字节数组
     * 
     * @param byte[] 需加密的字节数组
     * @return byte[] 加密后的字节数组
     * @throws Exception
     *             JAVA异常
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     * 将字符串转换为字节数组后重用encrypt(byte[])方法进行加密，并将得到字节数组转换为字符串返回
     * 
     * @param String
     *            需加密的字符串
     * @return String 加密后的字符串
     * @throws Exception
     *             JAVA异常
     */
    public String encrypt(String strIn) throws Exception {
        return byteGrpToHexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     * 
     * @param byte[] 需解密的字节数组
     * @return byte[] 解密后的字节数组
     * @throws Exception
     *             JAVA异常
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     * 将参数字符串转换为字节数组后重用decrypt(String)方法进行解密，并将得到的解密字符数组转换为字符串后返回。
     * 
     * @param String
     *            需解密的字符串
     * @return String 解密后的字符串
     * @throws Exception
     *             JAVA异常
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStrToByteGrp(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * 
     * @param byte[] 构成该字符串的字节数组
     * @return Key 生成的密钥
     * @throws Exception
     *             JAVA异常
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }
}
