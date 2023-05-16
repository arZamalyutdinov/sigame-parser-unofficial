package com.sigame.unofficial

import com.sigame.unofficial.entity.Game
import com.sigame.unofficial.entity.Question
import com.sigame.unofficial.entity.Round
import com.sigame.unofficial.entity.Theme
import com.sigame.unofficial.utils.firstMapped
import com.sigame.unofficial.utils.forEachCasted
import com.sigame.unofficial.utils.orThrow
import com.sigame.unofficial.utils.unzipFile
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import java.io.File
import java.io.FileInputStream
import java.util.UUID

class SigameParser(private val destinationDir: String,
                   private val file: File) {

    fun parseContents(): Game {
        val directory = File(destinationDir)
        directory.mkdir()
        unzipFile(file, directory)

        val contentStream = FileInputStream(directory.absolutePath + "/content.xml")
        val builder = SAXBuilder()
        val document: Document = builder.build(contentStream)

        val rootElement: Element = document.rootElement
        println("Root element name: ${rootElement.name}")

        val childElements: List<Element> = rootElement.children
        val t = childElements.find { it.name == "rounds" }.orThrow().content

        val rounds = mutableListOf<Round>()
        t.forEachCasted {
            val themeEntities = mutableListOf<Theme>()
            val themes = (it.content.first() as Element).content
            themes.forEachCasted {theme ->

                val name = theme.attributes.first().value
                val questionList = mutableListOf<Question>()
                (theme.content.find { themeContent -> (themeContent as Element).name == "questions" } as Element).content.forEachCasted { question ->
                    if (question.name != "comments") {
                        val price = question.attributes.find { it.name == "price" }?.value?.toInt().orThrow()
                        val t1 = question.content.firstMapped().content.firstMapped()
                        val type = question.content.firstMapped().content.firstMapped().getAttribute("type")?.value
                        val contentVal = t1.content.first().value
                        println(question)
                    }
                }
                themeEntities.add(Theme(name, questionList))
            }
            rounds.add(Round(themeEntities))
        }
        return Game(rounds)
    }

}