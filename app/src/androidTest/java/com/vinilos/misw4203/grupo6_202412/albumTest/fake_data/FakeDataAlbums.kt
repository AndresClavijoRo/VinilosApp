package com.vinilos.misw4203.grupo6_202412.albumTest.fake_data

import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
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
            tracks = tracks
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
            tracks =  arrayListOf()
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
