package com.sigame.unofficial.content

interface Content {

    fun getContent(): ByteArray

    fun getExtension(): ContentType

}