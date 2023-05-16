package com.sigame.unofficial.utils

import org.jdom2.Element
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


fun <T> T?.orThrow(e: Exception = RuntimeException()) = this ?: throw e

inline fun <T> Iterable<T>.forEachCasted(action: (Element) -> Unit) {

    for (element in this) {
        println(this.javaClass)
        if (this is Element) {
            action(element as Element)
        }
    }
}

fun <T> List<T>.firstMapped() = this.first() as Element

fun unzipFile(zipFile: File, destDir: File) {

    val buffer = ByteArray(1024)
    val zis = ZipInputStream(FileInputStream(zipFile))
    var zipEntry = zis.nextEntry
    while (zipEntry != null) {
        while (zipEntry != null) {
            val newFile: File = newFile(destDir, zipEntry)
            if (zipEntry.isDirectory) {
                if (!newFile.isDirectory && !newFile.mkdirs()) {
                    throw IOException("Failed to create directory $newFile")
                }
            } else {
                val parent = newFile.parentFile
                if (!parent.isDirectory && !parent.mkdirs()) {
                    throw IOException("Failed to create directory $parent")
                }

                val fos = FileOutputStream(newFile)
                var len: Int
                while (zis.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
            }
            zipEntry = zis.nextEntry
        }
    }

    zis.closeEntry()
    zis.close()
}

fun newFile(destinationDir: File, zipEntry: ZipEntry): File {
    val destFile = File(destinationDir, zipEntry.name)
    val destDirPath = destinationDir.canonicalPath
    val destFilePath = destFile.canonicalPath
    if (!destFilePath.startsWith(destDirPath + File.separator)) {
        throw IOException("Entry is outside of the target dir: " + zipEntry.name)
    }
    return destFile
}

