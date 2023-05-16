package com.sigame.unofficial.content

class TextContent(private val contents: String) : Content {

    override fun getContent(): ByteArray {
        return contents.toByteArray()
    }

    override fun getExtension(): ContentType {
        return ContentType.TEXT
    }
}