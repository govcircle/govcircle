package gov.govcircle.common.initialsetup;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class CborSimpleEncoder {

    private CborSimpleEncoder() {}

    /** Public: encode a CBOR Text String (major type 3) from a Java String. */
    public static byte[] encodeTextString(String text) {
        byte[] payload = text.getBytes(StandardCharsets.UTF_8);
        return encodeMajorTypeWithPayload(3, payload);
    }

    /** Public: encode a CBOR Byte String (major type 2) from raw bytes. */
    public static byte[] encodeByteString(byte[] bytes) {
        return encodeMajorTypeWithPayload(2, bytes);
    }

    /** Public: encode an indefinite-length CBOR text string composed of chunks. */
    public static byte[] encodeIndefiniteTextString(List<String> chunks) {
        return encodeIndefinite(3, chunks.stream()
                .map(s -> s.getBytes(StandardCharsets.UTF_8))
                .toArray(byte[][]::new));
    }

    /** Public: encode an indefinite-length CBOR byte string composed of chunks. */
    public static byte[] encodeIndefiniteByteString(List<byte[]> chunks) {
        return encodeIndefinite(2, chunks.toArray(new byte[0][]));
    }

    /* ----------------- Internal helpers ----------------- */

    // Compose header + payload for a definite-length item.
    private static byte[] encodeMajorTypeWithPayload(int majorType, byte[] payload) {
        byte[] header = encodeMajorTypeLengthHeader(majorType, payload.length);
        byte[] out = new byte[header.length + payload.length];
        System.arraycopy(header, 0, out, 0, header.length);
        System.arraycopy(payload, 0, out, header.length, payload.length);
        return out;
    }

    // Encode indefinite-length: start, then chunk items (each as definite-length of same major type), then break (0xFF).
    private static byte[] encodeIndefinite(int majorType, byte[][] chunks) {
        // start byte: (majorType << 5) | 31
        byte start = (byte) ((majorType << 5) | 0x1F);
        // compute total length
        int total = 1 + 1; // start + break (we will subtract/add properly)
        byte[][] encodedChunks = new byte[chunks.length][];
        for (int i = 0; i < chunks.length; i++) {
            encodedChunks[i] = encodeMajorTypeWithPayload(majorType, chunks[i]);
            total += encodedChunks[i].length;
        }
        byte[] out = new byte[total];
        int pos = 0;
        out[pos++] = start;
        for (byte[] c : encodedChunks) {
            System.arraycopy(c, 0, out, pos, c.length);
            pos += c.length;
        }
        out[pos++] = (byte) 0xFF; // break
        return out;
    }

    // Build the CBOR initial header bytes for given major type and payload length.
    private static byte[] encodeMajorTypeLengthHeader(int majorType, long length) {
        if (length < 0) throw new IllegalArgumentException("negative length");
        if (length <= 23) {
            byte hdr = (byte) ((majorType << 5) | (int) length);
            return new byte[] { hdr };
        } else if (length <= 0xFFL) {
            byte hdr = (byte) ((majorType << 5) | 24); // ai = 24 -> next 1 byte
            return new byte[] { hdr, (byte) (length & 0xFF) };
        } else if (length <= 0xFFFFL) {
            byte hdr = (byte) ((majorType << 5) | 25); // ai = 25 -> next 2 bytes
            ByteBuffer bb = ByteBuffer.allocate(1 + 2);
            bb.put(hdr);
            bb.putShort((short) (length & 0xFFFF));
            return bb.array();
        } else if (length <= 0xFFFFFFFFL) {
            byte hdr = (byte) ((majorType << 5) | 26); // ai = 26 -> next 4 bytes
            ByteBuffer bb = ByteBuffer.allocate(1 + 4);
            bb.put(hdr);
            bb.putInt((int) (length & 0xFFFFFFFF));
            return bb.array();
        } else {
            byte hdr = (byte) ((majorType << 5) | 27); // ai = 27 -> next 8 bytes
            ByteBuffer bb = ByteBuffer.allocate(1 + 8);
            bb.put(hdr);
            bb.putLong(length);
            return bb.array();
        }
    }

    /* ----------------- Utilities for demo ----------------- */

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
            sb.append(' ');
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /** Demo / quick test */
    public static void main(String[] args) {
        byte[] tstr = encodeTextString("hello");
        byte[] bstr = encodeByteString("hello".getBytes(StandardCharsets.UTF_8));

        System.out.println("TSTR (text)  hex: " + toHex(tstr)); // expect: 65 68 65 6c 6c 6f
        System.out.println("BSTR (bytes) hex: " + toHex(bstr)); // expect: 45 68 65 6c 6c 6f

        // indefinite byte string using two chunks "he" + "llo"
        byte[] indefBstr = encodeIndefiniteByteString(List.of("he".getBytes(), "llo".getBytes()));
        System.out.println("Indef BSTR hex: " + toHex(indefBstr));

        // indefinite text string
        byte[] indefTstr = encodeIndefiniteTextString(List.of("he", "llo"));
        System.out.println("Indef TSTR hex: " + toHex(indefTstr));
    }
}
