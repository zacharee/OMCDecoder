package tk.zwander.common.util

import com.soywiz.korio.async.use
import com.soywiz.korio.async.useIt
import com.soywiz.korio.stream.bufferedInput
import com.soywiz.korio.stream.readBytesExact
import com.soywiz.korio.stream.readLine
import tk.zwander.common.data.File
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class Coder {
    companion object {
        private const val SALT_LENGTH = 256
        private const val XML_HEADER = "<?xml"
    }

    private val salts = byteArrayOf(
        65.toByte(),
        (-59).toByte(),
        33.toByte(),
        (-34).toByte(),
        107.toByte(),
        28.toByte(),
        (-107).toByte(),
        55.toByte(),
        78.toByte(),
        17.toByte(),
        (-81).toByte(),
        6.toByte(),
        (-80).toByte(),
        (-121).toByte(),
        (-35).toByte(),
        (-23).toByte(),
        72.toByte(),
        122.toByte(),
        (-63).toByte(),
        (-43).toByte(),
        68.toByte(),
        119.toByte(),
        (-78).toByte(),
        (-111).toByte(),
        (-60).toByte(),
        31.toByte(),
        60.toByte(),
        57.toByte(),
        92.toByte(),
        (-88).toByte(),
        (-100).toByte(),
        (-69).toByte(),
        (-106).toByte(),
        91.toByte(),
        69.toByte(),
        93.toByte(),
        110.toByte(),
        23.toByte(),
        93.toByte(),
        53.toByte(),
        (-44).toByte(),
        (-51).toByte(),
        64.toByte(),
        (-80).toByte(),
        46.toByte(),
        2.toByte(),
        (-4).toByte(),
        12.toByte(),
        (-45).toByte(),
        80.toByte(),
        (-44).toByte(),
        (-35).toByte(),
        (-111).toByte(),
        (-28).toByte(),
        (-66).toByte(),
        (-116).toByte(),
        39.toByte(),
        2.toByte(),
        (-27).toByte(),
        (-45).toByte(),
        (-52).toByte(),
        125.toByte(),
        39.toByte(),
        66.toByte(),
        (-90).toByte(),
        63.toByte(),
        (-105).toByte(),
        (-67).toByte(),
        84.toByte(),
        (-57).toByte(),
        (-4).toByte(),
        (-4).toByte(),
        101.toByte(),
        (-90).toByte(),
        81.toByte(),
        10.toByte(),
        (-33).toByte(),
        1.toByte(),
        67.toByte(),
        (-57).toByte(),
        (-71).toByte(),
        18.toByte(),
        (-74).toByte(),
        102.toByte(),
        96.toByte(),
        (-89).toByte(),
        64.toByte(),
        (-17).toByte(),
        54.toByte(),
        (-94).toByte(),
        (-84).toByte(),
        (-66).toByte(),
        14.toByte(),
        119.toByte(),
        121.toByte(),
        2.toByte(),
        (-78).toByte(),
        (-79).toByte(),
        89.toByte(),
        63.toByte(),
        93.toByte(),
        109.toByte(),
        (-78).toByte(),
        (-51).toByte(),
        66.toByte(),
        (-36).toByte(),
        32.toByte(),
        86.toByte(),
        3.toByte(),
        (-58).toByte(),
        (-15).toByte(),
        92.toByte(),
        58.toByte(),
        2.toByte(),
        (-89).toByte(),
        (-80).toByte(),
        (-13).toByte(),
        (-1).toByte(),
        122.toByte(),
        (-4).toByte(),
        48.toByte(),
        63.toByte(),
        (-44).toByte(),
        59.toByte(),
        100.toByte(),
        (-42).toByte(),
        (-45).toByte(),
        59.toByte(),
        (-7).toByte(),
        (-17).toByte(),
        (-54).toByte(),
        34.toByte(),
        (-54).toByte(),
        71.toByte(),
        (-64).toByte(),
        (-26).toByte(),
        (-87).toByte(),
        (-80).toByte(),
        (-17).toByte(),
        (-44).toByte(),
        (-38).toByte(),
        (-112).toByte(),
        70.toByte(),
        10.toByte(),
        (-106).toByte(),
        95.toByte(),
        (-24).toByte(),
        (-4).toByte(),
        (-118).toByte(),
        45.toByte(),
        (-85).toByte(),
        (-13).toByte(),
        85.toByte(),
        25.toByte(),
        (-102).toByte(),
        (-119).toByte(),
        13.toByte(),
        (-37).toByte(),
        116.toByte(),
        46.toByte(),
        (-69).toByte(),
        59.toByte(),
        42.toByte(),
        (-90).toByte(),
        (-38).toByte(),
        (-105).toByte(),
        101.toByte(),
        (-119).toByte(),
        (-36).toByte(),
        97.toByte(),
        (-3).toByte(),
        (-62).toByte(),
        (-91).toByte(),
        (-97).toByte(),
        (-125).toByte(),
        17.toByte(),
        14.toByte(),
        106.toByte(),
        (-72).toByte(),
        (-119).toByte(),
        99.toByte(),
        111.toByte(),
        20.toByte(),
        18.toByte(),
        (-27).toByte(),
        113.toByte(),
        64.toByte(),
        (-24).toByte(),
        74.toByte(),
        (-60).toByte(),
        (-100).toByte(),
        26.toByte(),
        56.toByte(),
        (-44).toByte(),
        (-70).toByte(),
        12.toByte(),
        (-51).toByte(),
        (-100).toByte(),
        (-32).toByte(),
        (-11).toByte(),
        26.toByte(),
        48.toByte(),
        (-117).toByte(),
        98.toByte(),
        (-93).toByte(),
        51.toByte(),
        (-25).toByte(),
        (-79).toByte(),
        (-31).toByte(),
        97.toByte(),
        87.toByte(),
        (-105).toByte(),
        (-64).toByte(),
        7.toByte(),
        (-13).toByte(),
        (-101).toByte(),
        33.toByte(),
        (-122).toByte(),
        5.toByte(),
        (-104).toByte(),
        89.toByte(),
        (-44).toByte(),
        (-117).toByte(),
        63.toByte(),
        (-80).toByte(),
        (-6).toByte(),
        (-71).toByte(),
        (-110).toByte(),
        (-29).toByte(),
        (-105).toByte(),
        116.toByte(),
        107.toByte(),
        (-93).toByte(),
        91.toByte(),
        (-41).toByte(),
        (-13).toByte(),
        20.toByte(),
        (-115).toByte(),
        (-78).toByte(),
        43.toByte(),
        79.toByte(),
        (-122).toByte(),
        6.toByte(),
        102.toByte(),
        (-32).toByte(),
        52.toByte(),
        (-118).toByte(),
        (-51).toByte(),
        72.toByte(),
        (-104).toByte(),
        41.toByte(),
        (-38).toByte(),
        124.toByte(),
        72.toByte(),
        (-126).toByte(),
        (-35).toByte(),
    )
    private val shifts = ByteArray(256).also { bArr ->
        bArr[0] = 1.toByte()
        bArr[1] = 1.toByte()
        bArr[3] = 2.toByte()
        bArr[4] = 2.toByte()
        bArr[5] = 4.toByte()
        bArr[6] = 5.toByte()
        bArr[8] = 4.toByte()
        bArr[9] = 7.toByte()
        bArr[10] = 1.toByte()
        bArr[11] = 6.toByte()
        bArr[12] = 5.toByte()
        bArr[13] = 3.toByte()
        bArr[14] = 3.toByte()
        bArr[15] = 1.toByte()
        bArr[16] = 2.toByte()
        bArr[17] = 5.toByte()
        bArr[19] = 6.toByte()
        bArr[20] = 2.toByte()
        bArr[21] = 2.toByte()
        bArr[22] = 4.toByte()
        bArr[23] = 2.toByte()
        bArr[24] = 2.toByte()
        bArr[25] = 3.toByte()
        bArr[27] = 2.toByte()
        bArr[28] = 1.toByte()
        bArr[29] = 2.toByte()
        bArr[30] = 4.toByte()
        bArr[31] = 3.toByte()
        bArr[32] = 4.toByte()
        bArr[36] = 3.toByte()
        bArr[37] = 5.toByte()
        bArr[38] = 3.toByte()
        bArr[39] = 1.toByte()
        bArr[40] = 6.toByte()
        bArr[41] = 5.toByte()
        bArr[42] = 6.toByte()
        bArr[43] = 1.toByte()
        bArr[44] = 1.toByte()
        bArr[45] = 1.toByte()
        bArr[48] = 3.toByte()
        bArr[49] = 2.toByte()
        bArr[50] = 7.toByte()
        bArr[51] = 7.toByte()
        bArr[52] = 5.toByte()
        bArr[53] = 6.toByte()
        bArr[54] = 7.toByte()
        bArr[55] = 3.toByte()
        bArr[56] = 5.toByte()
        bArr[57] = 1.toByte()
        bArr[59] = 7.toByte()
        bArr[60] = 6.toByte()
        bArr[61] = 3.toByte()
        bArr[62] = 6.toByte()
        bArr[63] = 5.toByte()
        bArr[64] = 4.toByte()
        bArr[65] = 5.toByte()
        bArr[66] = 3.toByte()
        bArr[67] = 5.toByte()
        bArr[68] = 1.toByte()
        bArr[69] = 3.toByte()
        bArr[70] = 3.toByte()
        bArr[71] = 1.toByte()
        bArr[72] = 5.toByte()
        bArr[73] = 4.toByte()
        bArr[74] = 1.toByte()
        bArr[77] = 2.toByte()
        bArr[78] = 6.toByte()
        bArr[79] = 6.toByte()
        bArr[80] = 6.toByte()
        bArr[81] = 6.toByte()
        bArr[82] = 4.toByte()
        bArr[84] = 1.toByte()
        bArr[85] = 1.toByte()
        bArr[87] = 5.toByte()
        bArr[88] = 5.toByte()
        bArr[89] = 4.toByte()
        bArr[90] = 2.toByte()
        bArr[91] = 4.toByte()
        bArr[92] = 6.toByte()
        bArr[93] = 1.toByte()
        bArr[94] = 7.toByte()
        bArr[95] = 1.toByte()
        bArr[96] = 2.toByte()
        bArr[97] = 1.toByte()
        bArr[98] = 1.toByte()
        bArr[99] = 6.toByte()
        bArr[100] = 5.toByte()
        bArr[101] = 4.toByte()
        bArr[102] = 7.toByte()
        bArr[103] = 6.toByte()
        bArr[104] = 5.toByte()
        bArr[105] = 1.toByte()
        bArr[106] = 6.toByte()
        bArr[107] = 7.toByte()
        bArr[109] = 2.toByte()
        bArr[110] = 6.toByte()
        bArr[111] = 3.toByte()
        bArr[112] = 1.toByte()
        bArr[113] = 7.toByte()
        bArr[114] = 1.toByte()
        bArr[115] = 1.toByte()
        bArr[116] = 7.toByte()
        bArr[117] = 4.toByte()
        bArr[119] = 4.toByte()
        bArr[120] = 2.toByte()
        bArr[121] = 5.toByte()
        bArr[122] = 3.toByte()
        bArr[123] = 1.toByte()
        bArr[124] = 1.toByte()
        bArr[125] = 5.toByte()
        bArr[126] = 6.toByte()
        bArr[128] = 3.toByte()
        bArr[129] = 5.toByte()
        bArr[130] = 3.toByte()
        bArr[131] = 6.toByte()
        bArr[132] = 5.toByte()
        bArr[133] = 7.toByte()
        bArr[134] = 2.toByte()
        bArr[135] = 5.toByte()
        bArr[136] = 6.toByte()
        bArr[137] = 6.toByte()
        bArr[138] = 2.toByte()
        bArr[139] = 2.toByte()
        bArr[140] = 3.toByte()
        bArr[141] = 6.toByte()
        bArr[143] = 4.toByte()
        bArr[144] = 3.toByte()
        bArr[145] = 2.toByte()
        bArr[147] = 2.toByte()
        bArr[148] = 2.toByte()
        bArr[149] = 3.toByte()
        bArr[150] = 5.toByte()
        bArr[151] = 3.toByte()
        bArr[152] = 3.toByte()
        bArr[153] = 2.toByte()
        bArr[154] = 5.toByte()
        bArr[155] = 5.toByte()
        bArr[156] = 5.toByte()
        bArr[157] = 1.toByte()
        bArr[158] = 3.toByte()
        bArr[159] = 1.toByte()
        bArr[160] = 1.toByte()
        bArr[161] = 1.toByte()
        bArr[162] = 4.toByte()
        bArr[163] = 5.toByte()
        bArr[164] = 1.toByte()
        bArr[165] = 6.toByte()
        bArr[166] = 2.toByte()
        bArr[167] = 4.toByte()
        bArr[168] = 7.toByte()
        bArr[169] = 1.toByte()
        bArr[170] = 4.toByte()
        bArr[171] = 6.toByte()
        bArr[173] = 6.toByte()
        bArr[174] = 4.toByte()
        bArr[175] = 3.toByte()
        bArr[176] = 2.toByte()
        bArr[177] = 6.toByte()
        bArr[178] = 1.toByte()
        bArr[179] = 6.toByte()
        bArr[180] = 3.toByte()
        bArr[181] = 2.toByte()
        bArr[182] = 1.toByte()
        bArr[183] = 6.toByte()
        bArr[184] = 7.toByte()
        bArr[185] = 3.toByte()
        bArr[186] = 2.toByte()
        bArr[187] = 1.toByte()
        bArr[188] = 1.toByte()
        bArr[189] = 5.toByte()
        bArr[190] = 6.toByte()
        bArr[191] = 7.toByte()
        bArr[192] = 2.toByte()
        bArr[193] = 2.toByte()
        bArr[194] = 2.toByte()
        bArr[195] = 7.toByte()
        bArr[196] = 4.toByte()
        bArr[197] = 6.toByte()
        bArr[198] = 7.toByte()
        bArr[199] = 5.toByte()
        bArr[200] = 3.toByte()
        bArr[201] = 1.toByte()
        bArr[202] = 4.toByte()
        bArr[203] = 2.toByte()
        bArr[204] = 7.toByte()
        bArr[205] = 1.toByte()
        bArr[206] = 6.toByte()
        bArr[207] = 2.toByte()
        bArr[208] = 4.toByte()
        bArr[209] = 1.toByte()
        bArr[210] = 5.toByte()
        bArr[211] = 6.toByte()
        bArr[212] = 5.toByte()
        bArr[213] = 4.toByte()
        bArr[214] = 5.toByte()
        bArr[216] = 1.toByte()
        bArr[217] = 1.toByte()
        bArr[218] = 6.toByte()
        bArr[219] = 3.toByte()
        bArr[220] = 7.toByte()
        bArr[221] = 2.toByte()
        bArr[223] = 2.toByte()
        bArr[224] = 5.toByte()
        bArr[226] = 1.toByte()
        bArr[227] = 3.toByte()
        bArr[228] = 3.toByte()
        bArr[229] = 2.toByte()
        bArr[230] = 6.toByte()
        bArr[231] = 7.toByte()
        bArr[232] = 7.toByte()
        bArr[233] = 2.toByte()
        bArr[234] = 5.toByte()
        bArr[235] = 6.toByte()
        bArr[237] = 4.toByte()
        bArr[238] = 1.toByte()
        bArr[239] = 2.toByte()
        bArr[240] = 5.toByte()
        bArr[241] = 3.toByte()
        bArr[242] = 7.toByte()
        bArr[243] = 6.toByte()
        bArr[244] = 5.toByte()
        bArr[245] = 2.toByte()
        bArr[246] = 5.toByte()
        bArr[247] = 2.toByte()
        bArr[249] = 1.toByte()
        bArr[250] = 3.toByte()
        bArr[251] = 1.toByte()
        bArr[252] = 4.toByte()
        bArr[253] = 3.toByte()
        bArr[254] = 4.toByte()
        bArr[255] = 2.toByte()
    }

    private fun decode(source: ByteArray): ByteArray {
        val results = ByteArray(source.size)
        for (i in source.indices) {
            results[i] = (((source[i].toInt() and 255)
                    shl shifts[i % 256].toInt())
                    or ((source[i].toInt() and 255)
                    ushr (8 - shifts[i % 256]))).toByte()
            results[i] = (results[i].toInt() xor salts[i % 256].toInt()).toByte()
        }

        return results
    }

    private fun encode(source: ByteArray): ByteArray {
        val results = ByteArray(source.size)
        for (i in source.indices) {
            results[i] = (source[i].toInt() xor salts[i % 256].toInt()).toByte()
            results[i] = (results[i].toInt() and 255 shr shifts[i % 256].toInt() or (results[i].toInt() and 255 shl 8 - shifts[i % 256])).toByte()
        }

        return results
    }

    private fun decompressGzip(sourceGz: ByteArray): ByteArray {
        val output = ByteArrayOutputStream()

        GZIPInputStream(sourceGz.inputStream()).use { input ->
            val buffer = ByteArray(1024)
            var nRead: Int

            while (true) {
                nRead = input.read(buffer, 0, buffer.size)

                if (nRead < 0) {
                    break
                }

                output.write(buffer, 0, nRead)
            }

            output.flush()
        }

        return output.toByteArray()
    }

    private fun compressGzip(data: ByteArray): ByteArray {
        val os = ByteArrayOutputStream(data.size)

        return object : GZIPOutputStream(os) {
            init {
                def.setLevel(Deflater.BEST_COMPRESSION)
            }
        }.use<GZIPOutputStream, ByteArray> { output ->
            output.write(data)
            output.finish()
            os.toByteArray()
        }
    }

    suspend fun isXmlEncoded(featureXml: File): Boolean {
        return try {
            featureXml.openInputStream().bufferedInput().useIt { input ->
                val headerStr = input.readLine()

                if (headerStr.contains(XML_HEADER)) {
                    return@useIt false
                }

                return@useIt true
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun decode(file: File): ByteArray {
        return decompressGzip(decode(file.openInputStream()
            .readBytesExact(file.length.toInt())))
    }

    fun decodeArray(encoded: ByteArray): ByteArray {
        return decompressGzip(decode(encoded))
    }

    suspend fun encode(file: File): ByteArray {
        return encode(compressGzip(file.openInputStream()
            .readBytesExact(file.length.toInt())))
    }

    fun encodeArray(decoded: ByteArray): ByteArray {
        return encode(compressGzip(decoded))
    }
}