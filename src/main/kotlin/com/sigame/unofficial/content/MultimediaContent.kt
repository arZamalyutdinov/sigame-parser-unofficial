package com.sigame.unofficial.content

import java.io.File

class MultimediaContent(private val filePath: String): Content {

    override fun getContent(): ByteArray {
        return File(filePath).readBytes()
    }

    override fun getExtension(): ContentType {
        return ContentType.valueOf(filePath.substringAfterLast("."))
    }

}