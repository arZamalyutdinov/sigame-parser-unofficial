import com.sigame.unofficial.content.MultimediaContent
import com.sigame.unofficial.content.TextContent
import com.sigame.unofficial.entity.Game
import com.sigame.unofficial.entity.Question
import com.sigame.unofficial.entity.Round
import com.sigame.unofficial.entity.Theme
import com.sigame.unofficial.entity.nested.QuestionType
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import com.sigame.unofficial.utils.firstMapped
import com.sigame.unofficial.utils.forEachCasted
import com.sigame.unofficial.utils.orThrow
import org.jdom2.Content

fun main(args: Array<String>) {
    val rs = object{}::class.java.classLoader.getResource("content.xml")
    val inputStream = object{}::class.java.classLoader.getResourceAsStream("content.xml")
    val builder = SAXBuilder()
    val document: Document = builder.build(inputStream)

    val rootElement: Element = document.rootElement
    println("Root element name: ${rootElement.name}")

    val childElements: List<Element> = rootElement.children
    val t = childElements.find { it.name == "rounds" }.orThrow().content

    val roundList = mutableListOf<Round>()
    t.forEach { round ->
        if (round.cType == Content.CType.Element) {
            val themeList = mutableListOf<Theme>()
            val themes = (round as Element).content.find { it.cType == Content.CType.Element }
            (themes as Element).content.forEach { theme ->
                if (theme.cType == Content.CType.Element) {
                    val curTheme = theme as Element
                    val questionList = mutableListOf<Question>()
                    val questions = curTheme.content.find { it.cType == Content.CType.Element}
                    (questions as Element).content.forEach { question ->
                        if (question.cType == Content.CType.Element) {
                            val curQuestion = question as Element
                            if (curQuestion.name != "comments") {

                                val price = curQuestion.getAttribute("price").value.toInt()
                                val contents = curQuestion.content.filter { it.cType == Content.CType.Element }
                                val tpe = contents.firstMapped().content.find { it.cType == Content.CType.Element }
                                val type = if (tpe != null) (tpe as Element).getAttribute("type")?.value ?: "text" else "text"
                                val content = (contents.firstMapped().content.find { it.cType == Content.CType.Element }
                                     as Element).content.first().value
                                if (type.lowercase() == "text") {
                                    questionList.add(
                                        Question(
                                            price,
                                            TextContent(content),
                                            "",
                                            "",
                                            QuestionType.DEFAULT
                                        )
                                    )
                                } else {
                                    questionList.add(
                                        Question(
                                            price,
                                            MultimediaContent(content),
                                            "",
                                            "",
                                            QuestionType.DEFAULT
                                        )
                                    )
                                }
                            }

                        }
                    }
                    themeList.add(Theme(curTheme.getAttribute("name").value, questionList))
                }
            }
            roundList.add(Round(themeList))
        }
    }
    val game = Game(roundList)
    println(game)
//    inputStream?.let {
//        val contents = it.bufferedReader().use { reader -> reader.readText() }
//        println(contents)
//    }

}