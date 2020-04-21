package com.ccte.core.util.digest;

import static com.ccte.core.util.BytesUtil.bytesToCCString;
import static com.ccte.core.util.BytesUtil.bytesToHEXString;
import static com.ccte.core.util.BytesUtil.bytesToHexString;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * © 2015-2019 Chenxj Copyright
 * 类    名：DigestUtils
 * 类 描 述：
 * 作    者：Chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2019年8月7日-下午3:10:35
 */
public class DigestUtils {
    private static final int BUFFER_LENGTH = 1024;
    private static final Charset UTF_8=Charset.forName("UTF-8");
    public static MessageDigest updateDigest(final MessageDigest digest, final InputStream data) throws IOException {
        final byte[] buffer = new byte[BUFFER_LENGTH];
        int read = data.read(buffer, 0, BUFFER_LENGTH);
        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, BUFFER_LENGTH);
        }
        return digest;
    }
    public static MessageDigest updateDigest(final MessageDigest digest, final InputStream data,final BytePipe pipe) throws IOException {
        final byte[] buffer = new byte[BUFFER_LENGTH];
        int read = data.read(buffer, 0, BUFFER_LENGTH);
        while (read > -1) {
        	pipe.pipe(buffer, read);
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, BUFFER_LENGTH);
        }
        return digest;
    }
    public static byte[] digest(final MessageDigest messageDigest, final InputStream data) throws IOException {
        return updateDigest(messageDigest, data).digest();
    }
    public static byte[] digest(final MessageDigest messageDigest, final InputStream data,final BytePipe pipe) throws IOException {
        return updateDigest(messageDigest, data, pipe).digest();
    }
    public static byte[] MD2(final byte[] data){
    	return MessageDigestAlgorithmsEnum.MD2.digest().digest(data);
    }
    public static String MD2HEX(final byte[] data){
    	return bytesToHEXString(MD2(data));
    }
    public static String MD2Hex(final byte[] data){
    	return bytesToHexString(MD2(data));
    }
    public static String MD2CC(final byte[] data) {
    	return bytesToCCString(MD2(data));
    }
    public static byte[] MD2(final String data) {
    	return MD2(data.getBytes(UTF_8));
    }
    public static String MD2HEX(final String data) {
    	return bytesToHEXString(MD2(data));
    }
    public static String MD2Hex(final String data) {
    	return bytesToHexString(MD2(data));
    }
    public static String MD2CC(final String data) {
    	return bytesToCCString(MD2(data));
    }
    public static byte[] MD2(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.MD2.digest(), data);
    }
    public static byte[] MD2(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.MD2.digest(), data, pipe);
    }
    public static String MD2HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(MD2(data));
    }
    public static String MD2HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(MD2(data, pipe));
    }
    public static String MD2Hex(final InputStream data) throws IOException {
    	return bytesToHexString(MD2(data));
    }
    public static String MD2Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(MD2(data, pipe));
    }
    public static String MD2CC(final InputStream data) throws IOException {
    	return bytesToCCString(MD2(data));
    }
    public static String MD2CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(MD2(data, pipe));
    }
    public static byte[] MD5(final byte[] data){
    	return MessageDigestAlgorithmsEnum.MD5.digest().digest(data);
    }
    public static String MD5HEX(final byte[] data){
    	return bytesToHEXString(MD5(data));
    }
    public static String MD5Hex(final byte[] data){
    	return bytesToHexString(MD5(data));
    }
    public static String MD5CC(final byte[] data){
    	return bytesToCCString(MD5(data));
    }
    public static byte[] MD5(final String data) {
    	return MD5(data.getBytes(UTF_8));
    }
    public static String MD5HEX(final String data) {
    	return bytesToHEXString(MD5(data));
    }
    public static String MD5Hex(final String data) {
    	return bytesToHexString(MD5(data));
    }
    public static String MD5CC(final String data) {
    	return bytesToCCString(MD5(data));
    }
    public static byte[] MD5(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.MD5.digest(), data);
    }
    public static byte[] MD5(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.MD5.digest(), data, pipe);
    }
    public static String MD5HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(MD5(data));
    }
    public static String MD5HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(MD5(data, pipe));
    }
    public static String MD5Hex(final InputStream data) throws IOException {
    	return bytesToHexString(MD5(data));
    }
    public static String MD5Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(MD5(data, pipe));
    }
    public static String MD5CC(final InputStream data) throws IOException {
    	return bytesToCCString(MD5(data));
    }
    public static String MD5CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(MD5(data, pipe));
    }
    public static byte[] SHA_1(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA_1.digest().digest(data);
    }
    public static String SHA_1HEX(final byte[] data){
    	return bytesToHEXString(SHA_1(data));
    }
    public static String SHA_1Hex(final byte[] data){
    	return bytesToHexString(SHA_1(data));
    }
    public static String SHA_1CC(final byte[] data){
    	return bytesToCCString(SHA_1(data));
    }
    public static byte[] SHA_1(final String data) {
    	return SHA_1(data.getBytes(UTF_8));
    }
    public static String SHA_1HEX(final String data) {
    	return bytesToHEXString(SHA_1(data));
    }
    public static String SHA_1Hex(final String data) {
    	return bytesToHexString(SHA_1(data));
    }
    public static String SHA_1CC(final String data) {
    	return bytesToCCString(SHA_1(data));
    }
    public static byte[] SHA_1(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_1.digest(), data);
    }
    public static byte[] SHA_1(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_1.digest(), data, pipe);
    }
    public static String SHA_1HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA_1(data));
    }
    public static String SHA_1HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA_1(data, pipe));
    }
    public static String SHA_1Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA_1(data));
    }
    public static String SHA_1Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA_1(data, pipe));
    }
    public static String SHA_1CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA_1(data));
    }
    public static String SHA_1CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA_1(data, pipe));
    }
    public static byte[] SHA_224(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA_224.digest().digest(data);
    }
    public static String SHA_224HEX(final byte[] data){
    	return bytesToHEXString(SHA_224(data));
    }
    public static String SHA_224Hex(final byte[] data){
    	return bytesToHexString(SHA_224(data));
    }
    public static String SHA_224CC(final byte[] data){
    	return bytesToCCString(SHA_224(data));
    }
    public static byte[] SHA_224(final String data) {
    	return SHA_224(data.getBytes(UTF_8));
    }
    public static String SHA_224HEX(final String data) {
    	return bytesToHEXString(SHA_224(data));
    }
    public static String SHA_224Hex(final String data) {
    	return bytesToHexString(SHA_224(data));
    }
    public static String SHA_224CC(final String data) {
    	return bytesToCCString(SHA_224(data));
    }
    public static byte[] SHA_224(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_224.digest(), data);
    }
    public static byte[] SHA_224(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_224.digest(), data, pipe);
    }
    public static String SHA_224HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA_224(data));
    }
    public static String SHA_224HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA_224(data, pipe));
    }
    public static String SHA_224Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA_224(data));
    }
    public static String SHA_224Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA_224(data, pipe));
    }
    public static String SHA_224CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA_224(data));
    }
    public static String SHA_224CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA_224(data, pipe));
    }
    public static byte[] SHA_256(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA_256.digest().digest(data);
    }
    public static String SHA_256HEX(final byte[] data){
    	return bytesToHEXString(SHA_256(data));
    }
    public static String SHA_256Hex(final byte[] data){
    	return bytesToHexString(SHA_256(data));
    }
    public static String SHA_256CC(final byte[] data){
    	return bytesToCCString(SHA_256(data));
    }
    public static byte[] SHA_256(final String data) {
    	return SHA_256(data.getBytes(UTF_8));
    }
    public static String SHA_256HEX(final String data) {
    	return bytesToHEXString(SHA_256(data));
    }
    public static String SHA_256Hex(final String data) {
    	return bytesToHexString(SHA_256(data));
    }
    public static String SHA_256CC(final String data) {
    	return bytesToCCString(SHA_256(data));
    }
    public static byte[] SHA_256(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_256.digest(), data);
    }
    public static byte[] SHA_256(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_256.digest(), data, pipe);
    }
    public static String SHA_256HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA_256(data));
    }
    public static String SHA_256HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA_256(data, pipe));
    }
    public static String SHA_256Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA_256(data));
    }
    public static String SHA_256Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA_256(data, pipe));
    }
    public static String SHA_256CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA_256(data));
    }
    public static String SHA_256CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA_256(data, pipe));
    }
    public static byte[] SHA_384(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA_384.digest().digest(data);
    }
    public static String SHA_384HEX(final byte[] data){
    	return bytesToHEXString(SHA_384(data));
    }
    public static String SHA_384Hex(final byte[] data){
    	return bytesToHexString(SHA_384(data));
    }
    public static String SHA_384CC(final byte[] data){
    	return bytesToCCString(SHA_384(data));
    }
    public static byte[] SHA_384(final String data) {
    	return SHA_384(data.getBytes(UTF_8));
    }
    public static String SHA_384HEX(final String data) {
    	return bytesToHEXString(SHA_384(data));
    }
    public static String SHA_384Hex(final String data) {
    	return bytesToHexString(SHA_384(data));
    }
    public static String SHA_384CC(final String data) {
    	return bytesToCCString(SHA_384(data));
    }
    public static byte[] SHA_384(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_384.digest(), data);
    }
    public static byte[] SHA_384(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_384.digest(), data, pipe);
    }
    public static String SHA_384HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA_384(data));
    }
    public static String SHA_384HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA_384(data, pipe));
    }
    public static String SHA_384Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA_384(data));
    }
    public static String SHA_384Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA_384(data, pipe));
    }
    public static String SHA_384CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA_384(data));
    }
    public static String SHA_384CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA_384(data, pipe));
    }
    public static byte[] SHA_512(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA_512.digest().digest(data);
    }
    public static String SHA_512HEX(final byte[] data){
    	return bytesToHEXString(SHA_512(data));
    }
    public static String SHA_512Hex(final byte[] data){
    	return bytesToHexString(SHA_512(data));
    }
    public static String SHA_512CC(final byte[] data){
    	return bytesToCCString(SHA_512(data));
    }
    public static byte[] SHA_512(final String data) {
    	return SHA_512(data.getBytes(UTF_8));
    }
    public static String SHA_512HEX(final String data) {
    	return bytesToHEXString(SHA_512(data));
    }
    public static String SHA_512Hex(final String data) {
    	return bytesToHexString(SHA_512(data));
    }
    public static String SHA_512CC(final String data) {
    	return bytesToCCString(SHA_512(data));
    }
    public static byte[] SHA_512(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_512.digest(), data);
    }
    public static byte[] SHA_512(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA_512.digest(), data, pipe);
    }
    public static String SHA_512HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA_512(data));
    }
    public static String SHA_512HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA_512(data, pipe));
    }
    public static String SHA_512Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA_512(data));
    }
    public static String SHA_512Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA_512(data, pipe));
    }
    public static String SHA_512CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA_512(data));
    }
    public static String SHA_512CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA_512(data, pipe));
    }
    public static byte[] SHA3_224(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA3_224.digest().digest(data);
    }
    public static String SHA3_224HEX(final byte[] data){
    	return bytesToHEXString(SHA3_224(data));
    }
    public static String SHA3_224Hex(final byte[] data){
    	return bytesToHexString(SHA3_224(data));
    }
    public static String SHA3_224CC(final byte[] data){
    	return bytesToCCString(SHA3_224(data));
    }
    public static byte[] SHA3_224(final String data) {
    	return SHA3_224(data.getBytes(UTF_8));
    }
    public static String SHA3_224HEX(final String data) {
    	return bytesToHEXString(SHA3_224(data));
    }
    public static String SHA3_224Hex(final String data) {
    	return bytesToHexString(SHA3_224(data));
    }
    public static String SHA3_224CC(final String data) {
    	return bytesToCCString(SHA3_224(data));
    }
    public static byte[] SHA3_224(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA3_224.digest(), data);
    }
    public static byte[] SHA3_224(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA3_224.digest(), data, pipe);
    }
    public static String SHA3_224HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA3_224(data));
    }
    public static String SHA3_224HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA3_224(data, pipe));
    }
    public static String SHA3_224Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA3_224(data));
    }
    public static String SHA3_224Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA3_224(data, pipe));
    }
    public static String SHA3_224CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA3_224(data));
    }
    public static String SHA3_224CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA3_224(data, pipe));
    }
    public static byte[] SHA3_256(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA3_256.digest().digest(data);
    }
    public static String SHA3_256HEX(final byte[] data){
    	return bytesToHEXString(SHA3_256(data));
    }
    public static String SHA3_256Hex(final byte[] data){
    	return bytesToHexString(SHA3_256(data));
    }
    public static String SHA3_256CC(final byte[] data){
    	return bytesToCCString(SHA3_256(data));
    }
    public static byte[] SHA3_256(final String data) {
    	return SHA3_256(data.getBytes(UTF_8));
    }
    public static String SHA3_256HEX(final String data) {
    	return bytesToHEXString(SHA3_256(data));
    }
    public static String SHA3_256Hex(final String data) {
    	return bytesToHexString(SHA3_256(data));
    }
    public static String SHA3_256CC(final String data) {
    	return bytesToCCString(SHA3_256(data));
    }
    public static byte[] SHA3_256(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA3_256.digest(), data);
    }
    public static byte[] SHA3_256(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA3_256.digest(), data, pipe);
    }
    public static String SHA3_256HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA3_256(data));
    }
    public static String SHA3_256HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA3_256(data, pipe));
    }
    public static String SHA3_256Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA3_256(data));
    }
    public static String SHA3_256Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA3_256(data, pipe));
    }
    public static String SHA3_256CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA3_256(data));
    }
    public static String SHA3_256CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA3_256(data, pipe));
    }
    public static byte[] SHA3_384(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA3_384.digest().digest(data);
    }
    public static String SHA3_384HEX(final byte[] data){
    	return bytesToHEXString(SHA3_384(data));
    }
    public static String SHA3_384Hex(final byte[] data){
    	return bytesToHexString(SHA3_384(data));
    }
    public static String SHA3_384CC(final byte[] data){
    	return bytesToCCString(SHA3_384(data));
    }
    public static byte[] SHA3_384(final String data) {
    	return SHA3_384(data.getBytes(UTF_8));
    }
    public static String SHA3_384HEX(final String data) {
    	return bytesToHEXString(SHA3_384(data));
    }
    public static String SHA3_384Hex(final String data) {
    	return bytesToHexString(SHA3_384(data));
    }
    public static String SHA3_384CC(final String data) {
    	return bytesToCCString(SHA3_384(data));
    }
    public static byte[] SHA3_384(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA3_384.digest(), data);
    }
    public static byte[] SHA3_384(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA3_384.digest(), data, pipe);
    }
    public static String SHA3_384HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA3_384(data));
    }
    public static String SHA3_384HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA3_384(data, pipe));
    }
    public static String SHA3_384Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA3_384(data));
    }
    public static String SHA3_384Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA3_384(data, pipe));
    }
    public static String SHA3_384CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA3_384(data));
    }
    public static String SHA3_384CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA3_384(data, pipe));
    }
    public static byte[] SHA3_512(final byte[] data){
    	return MessageDigestAlgorithmsEnum.SHA3_512.digest().digest(data);
    }
    public static String SHA3_512HEX(final byte[] data){
    	return bytesToHEXString(SHA3_512(data));
    }
    public static String SHA3_512Hex(final byte[] data){
    	return bytesToHexString(SHA3_512(data));
    }
    public static String SHA3_512CC(final byte[] data){
    	return bytesToCCString(SHA3_512(data));
    }
    public static byte[] SHA3_512(final String data) {
    	return SHA3_512(data.getBytes(UTF_8));
    }
    public static String SHA3_512HEX(final String data) {
    	return bytesToHEXString(SHA3_512(data));
    }
    public static String SHA3_512Hex(final String data) {
    	return bytesToHexString(SHA3_512(data));
    }
    public static String SHA3_512CC(final String data) {
    	return bytesToCCString(SHA3_512(data));
    }
    public static byte[] SHA3_512(final InputStream data) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA3_512.digest(), data);
    }
    public static byte[] SHA3_512(final InputStream data,final BytePipe pipe) throws IOException {
    	return digest(MessageDigestAlgorithmsEnum.SHA3_512.digest(), data, pipe);
    }
    public static String SHA3_512HEX(final InputStream data) throws IOException {
    	return bytesToHEXString(SHA3_512(data));
    }
    public static String SHA3_512HEX(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHEXString(SHA3_512(data, pipe));
    }
    public static String SHA3_512Hex(final InputStream data) throws IOException {
    	return bytesToHexString(SHA3_512(data));
    }
    public static String SHA3_512Hex(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToHexString(SHA3_512(data, pipe));
    }
    public static String SHA3_512CC(final InputStream data) throws IOException {
    	return bytesToCCString(SHA3_512(data));
    }
    public static String SHA3_512CC(final InputStream data,final BytePipe pipe) throws IOException {
    	return bytesToCCString(SHA3_512(data, pipe));
    }
    public static interface BytePipe{
    	public void pipe(byte[]bs,int size)throws IOException;
    }
}
