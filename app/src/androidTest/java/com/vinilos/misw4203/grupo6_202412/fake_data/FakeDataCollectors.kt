package com.vinilos.misw4203.grupo6_202412.fake_data

import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorAlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CommentDto

object FakeDataCollectors {
    val collectors = listOf(
        CollectorDto(
            id = 100,
            name = "Juan Perez",
            telephone = "+11231231234",
            email = "juan@test.com",
            comments = arrayListOf<CommentDto>(),
            favoritePerformers = arrayListOf<ArtistDto>(),
            collectorAlbums = arrayListOf<CollectorAlbumDto>()
        ),
        CollectorDto(
            id = 101,
            name = "Maria Lopez",
            telephone = "+11231231235",
            email = "maria@test.com",
            comments = arrayListOf<CommentDto>(),
            favoritePerformers = arrayListOf<ArtistDto>(),
            collectorAlbums = arrayListOf<CollectorAlbumDto>()
        ),
        CollectorDto(
            id = 102,
            name = "Pedro Ramirez",
            telephone = "+11231231236",
            email = "pedro@test.com",
            comments = arrayListOf<CommentDto>(),
            favoritePerformers = arrayListOf<ArtistDto>(),
            collectorAlbums = arrayListOf<CollectorAlbumDto>()
        ),
        CollectorDto(
            id = 103,
            name = "Ana Rodriguez",
            telephone = "+11231231237",
            email = "ana@test.com",
            comments = arrayListOf<CommentDto>(),
            favoritePerformers = arrayListOf<ArtistDto>(),
            collectorAlbums = arrayListOf<CollectorAlbumDto>()
        ),
        CollectorDto(
            id = 104,
            name = "Carlos Sanchez",
            telephone = "+11231231238",
            email = "carlos@test.com",
            comments = arrayListOf<CommentDto>(),
            favoritePerformers = arrayListOf<ArtistDto>(),
            collectorAlbums = arrayListOf<CollectorAlbumDto>()
        )
    )

    val artists: () -> ArrayList<ArtistDto> = {
        val artists = arrayListOf<ArtistDto>()
        for (i in 1..10) {
            artists.add(
                ArtistDto(
                    id = i,
                    name = "$i Artist",
                    image = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
                    description = "Artist $i description",
                    birthDate = "1948-07-16T00:00:00.000Z"
                )
            )
        }
        artists
    }
}