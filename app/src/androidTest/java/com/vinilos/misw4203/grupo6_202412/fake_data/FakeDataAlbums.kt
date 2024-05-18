package com.vinilos.misw4203.grupo6_202412.fake_data

import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CommentDto
import com.vinilos.misw4203.grupo6_202412.models.dto.TraksDto

object FakeDataAlbums {
    val albums = listOf(
        AlbumDto(
            id = 100,
            name = "Buscando América",
            cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = tracks,
            performers = performers,
            comments = comments
        ),
        AlbumDto(
            id = 101,
            name = "Poeta del pueblo",
            cover = "https://cdn.shopify.com/s/files/1/0275/3095/products/image_4931268b-7acf-4702-9c55-b2b3a03ed999_1024x1024.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Recopilación de 27 composiciones del cosmos Blades que los bailadores y melómanos han hecho suyas en estos 40 años de presencia de los ritmos y concordias afrocaribeños en múltiples escenarios internacionales. Grabaciones de Blades para la Fania con las orquestas de Pete Rodríguez, Ray Barreto, Fania All Stars y, sobre todo, los grandes éxitos con la Banda de Willie Colón",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = tracks
        ),
        AlbumDto(
            id = 33,
            name = "iyve71vqmz",
            cover = "123",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "primer album",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = arrayListOf()
        ),

        )
}


private val tracks = arrayListOf(
    TraksDto(
        id = 100,
        name = "Buscando América",
        duration = "00:04:00",
    ),
    TraksDto(
        id = 101,
        name = "Poeta del pueblo",
        duration = "00:04:00"
    )
)

private val performers = arrayListOf(
    ArtistDto(
        id = 100,
        name = "Rubén Blades",
        image = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
        description = "Rubén Blades Bellido de Luna (Ciudad de Panamá, 16 de julio de 1948) es un cantante, compositor, músico, actor, abogado, político y activista panameño, figura prominente de la música latina y la salsa. Ha ganado nueve premios Grammy y cinco premios Grammy Latino.",
        birthDate = "1948-07-16T00:00:00.000Z"
    ),
    ArtistDto(
        id = 101,
        name = "Willie Colón",
        image = "https://cdn.shopify.com/s/files/1/0275/3095/products/image_4931268b-7acf-4702-9c55-b2b3a03ed999_1024x1024.jpg",
        description = "Willie Colón (nacido el 28 de abril de 1950 en Nueva York) es un trombonista, cantante, compositor, productor y director de orquesta de salsa estadounidense de ascendencia puertorriqueña. Ha producido más de 40 álbumes y ha vendido más de 30 millones de discos en todo el mundo.",
        birthDate = "1950-04-28T00:00:00.000Z"
    )
)

private val comments = arrayListOf(
    CommentDto(
        id = 100,
        description = "The most relevant album of Ruben Blades",
        rating = 4,
    ),
    CommentDto(
        id = 101,
        description = "The best album of Ruben Blades",
        rating = 5
    )
)
