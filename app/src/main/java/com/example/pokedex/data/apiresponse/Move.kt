package com.example.pokedex.data.apiresponse

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)