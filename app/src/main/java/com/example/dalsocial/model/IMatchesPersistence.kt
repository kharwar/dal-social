package com.example.dalsocial.model

interface IMatchesPersistence {
    fun getAllMatches(result: (List<Match>) -> Unit)
}