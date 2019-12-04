package com.jem.testboardapp.data

data class Comment(
    var comment_id: Int,
    var board_id: Int,
    var comment_content: String,
    var comment_date_time: String?
)