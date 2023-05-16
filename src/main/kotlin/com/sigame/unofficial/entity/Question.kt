package com.sigame.unofficial.entity

import com.sigame.unofficial.content.Content
import com.sigame.unofficial.entity.nested.QuestionType

data class Question (val price: Int,
                     val content: Content,
                     val answer: String,
                     val comments: String? = null,
                     val type: QuestionType
)