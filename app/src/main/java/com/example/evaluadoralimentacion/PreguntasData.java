package com.example.evaluadoralimentacion;

import java.util.ArrayList;
import java.util.List;

public class PreguntasData {

    public static List<Pregunta> getPreguntas() {
        List<Pregunta> preguntas = new ArrayList<>();

        preguntas.add(new Pregunta(
                "¿Cuántas veces al día sueles comer?",
                new String[]{"1 vez", "2 veces", "3 veces", "4 o más"},
                new int[]{0, 3, 5, 4}
        ));

        preguntas.add(new Pregunta(
                "¿Sueles desayunar todos los días?",
                new String[]{"Si", "No"},
                new int[]{5, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Con qué frecuencia comes fuera de casa?",
                new String[]{"Nunca", "1-3 veces por mes", "1-2 veces por semana", "3-4 veces por semana", "5-6 veces por semana", "Todos los días"},
                new int[]{5, 4, 3, 2 ,1, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Cuántas veces a la semana consumes comida frita o muy grasosa?",
                new String[]{"Nunca", "1-2 veces", "3-4 veces", "5 o más veces"},
                new int[]{5, 3, 1, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Con qué frecuencia consumes alimentos ricos en fibra?",
                new String[]{"Todos los días", "5-6 veces por semana", "3-4 veces por semana", "1-2 veces por semana",
                             "1-3 veces al mes", "Nunca"},
                new int[]{5, 4, 3, 2, 1, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Consumes alcohol con frecuencia?",
                new String[]{"Nunca", "1-3 veces al mes", "1-2 veces por semana", "3-4 veces por semana",
                             "5-6 veces por semana", "Todos los días"},
                new int[]{5, 4, 2, 1, 0, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Haces actividad física regularmente?",
                new String[]{"Si", "No"},
                new int[]{5, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Cuánto tiempo dedicas a cocinar en casa diariamente?",
                new String[]{"Más de 60 min", "30-60 min", "Menos de 30 min", "No cocino"},
                new int[]{5, 4, 3, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Sueles añadir azúcar o miel a tus bebidas o alimentos?",
                new String[]{"Nunca", "1-3 veces al mes", "1-2 veces por semana", "3-4 veces por semana",
                             "5-6 veces por semana", "Todos los días"},
                new int[]{5, 4, 3, 2, 1, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Qué tipo de desayuno consumes más a menudo?",
                new String[]{"Tradicional (huevo, tortilla, frijol)", "Lácteos y fruta",
                             "Pan o cereales", "No desayuno"},
                new int[]{5, 4, 2, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Qué tipo de comida consumes más para la comida principal?",
                new String[]{"Platillos caseros", "Otros (Cocinas diversas como mediterránea, ensaladas)",
                             "Comida rápida mexicana (tacos, sopes, tortas, huaraches)",
                             "Restaurantes de comida rápida internacional (hamburguesas, pizza, comida china)"},
                new int[]{5, 3, 1, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Cuántas veces a la semana consumes bebidas energéticas o café?",
                new String[]{"Nunca", "1-2 veces", "3-4 veces", "5-6 veces", "Todos los días"},
                new int[]{5, 3, 1, 0, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Lees las etiquetas nutricionales antes de comprar alimentos?",
                new String[]{"Si, siempre", "A veces", "Nunca"},
                new int[]{5, 3, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Cuánto pan o tortillas consumes al día, en promedio?",
                new String[]{"Ninguno", "1–2 piezas/tortillas", "3–4 piezas/tortillas", "5 o más"},
                new int[]{3, 5, 2, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Consumes productos bajos en grasa o “light”?",
                new String[]{"Nunca", "1–3 veces al mes", "1–2 veces por semana", "3–4 veces por semana", "5–6 veces por semana", "Todos los días"},
                new int[]{5, 4, 3, 2, 1, 0}
        ));

        preguntas.add(new Pregunta(
                "¿Consumes suplementos?",
                new String[]{"Sí, regularmente", "A veces", "Nunca"},
                new int[]{5, 3, 0}
        ));

        String[] opciones = {
                "Nunca",
                "1–3 veces al mes",
                "1–2 veces por semana",
                "3–4 veces por semana",
                "5–6 veces por semana",
                "Todos los días"
        };

        int[] puntosSaludables = {0, 1, 2, 3, 4, 5};

        preguntas.add(new Pregunta("¿Con qué frecuencia consumes frutas (manzana, plátano, naranja, etc.)?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes verduras crudas (ensaladas, pepino, jitomate, etc.)?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes verduras cocidas (acelgas, espinacas, etc.)?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes cereales integrales (avena, pan integral, tortillas)?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes leguminosas (frijol, lenteja, garbanzo, habas y soya)?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes pollo o pavo?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes pescado?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes huevos?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes lácteos (leche, yogur, queso)?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes licuados?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes jugos naturales?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes frutos secos y semillas?", opciones, puntosSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes aceites y grasas añadidas (aceite, mantequilla, margarina)?", opciones, puntosSaludables));

        int[] puntosNoSaludables = {5, 4, 3, 2, 1, 0};

        preguntas.add(new Pregunta("¿Con qué frecuencia consumes postres (galletas, pan dulce, pastel, barras dulces, gelatinas, flan, tartas)?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes alimentos ultraprocesados (hot dogs, sopas instantáneas, nuggets, pizzas congeladas, arroz instantáneo, etc.)?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes snacks salados o frituras (Sabritas, churros)?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes dulces (chicles, paletas de caramelo y paletas heladas)?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes comida rápida mexicana (tacos, sopes, tortas, huaraches)?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes cereales refinados (Arroz blanco, pan blanco, pasta, tortilla de harina)?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes carne roja (res, cerdo, Borrego y chivo)?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes mariscos?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes bebidas azucaradas (refrescos, jugos envasados)?", opciones, puntosNoSaludables));
        preguntas.add(new Pregunta("¿Con qué frecuencia consumes lechitas, cafés industrializados?", opciones, puntosNoSaludables));

        return preguntas;
    }

    public static String clasificarAlimentacion(int puntajeTotal, int maxPuntaje) {
        double porcentaje = (puntajeTotal * 100.0) / maxPuntaje;

        if (porcentaje >= 80) {
            return "Alimentación Saludable";
        } else if (porcentaje >= 50) {
            return "Necesita Cambios";
        } else{
            return "Poco Saludable";
        }
    }
}