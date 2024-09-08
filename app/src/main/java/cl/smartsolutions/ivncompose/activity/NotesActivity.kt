package cl.smartsolutions.ivncompose.activity

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.smartsolutions.ivnapp.model.Note
import cl.smartsolutions.ivncompose.R
import cl.smartsolutions.ivncompose.ui.theme.IvnComposeTheme
import java.util.*

class NotesActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var textToSpeech: TextToSpeech
    private val notesList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        textToSpeech = TextToSpeech(this, this)

        setContent {
            IvnComposeTheme {
                NotesScreen(
                    notesList = notesList,
                    onAddNote = { startActivity(Intent(this, AddNoteActivity::class.java)) },
                    onReadNote = { note, locale -> readNoteContent(note, locale) }
                )
            }
        }

        loadNotes()
    }

    private fun loadNotes() {
        // Cargar las notas
        notesList.apply {
            add(Note("Pedido de información", "¿Puedes escribir lo que estás diciendo?"))
            add(Note("Explicación de sordera", "Soy sordo/a, no puedo escuchar. Por favor, lee mi mensaje."))
            add(Note("Pedido de bebida", "Me gustaría un vaso de agua, por favor."))
            add(Note("Gracias", "Muchas gracias por tu ayuda."))
            add(Note("Llamar la atención", "Disculpa, ¿puedes mirarme un momento?"))
            add(Note("Comunicación alternativa", "¿Podemos comunicarnos por escrito?"))
            add(Note("Pedido de comida", "Quisiera pedir una hamburguesa sin queso, por favor."))
            add(Note("Confirmación", "Sí, entiendo."))
            add(Note("Negación", "No, no necesito ayuda, gracias."))
            add(Note("Llamada de emergencia", "Por favor, llama al 133, hay una emergencia."))
            add(Note("Despedida", "Adiós, que tengas un buen día."))
            add(Note("Pregunta por tiempo", "¿Qué hora es?"))

        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.language = Locale("es", "ES")
            textToSpeech.setPitch(0.9f)
            textToSpeech.setSpeechRate(0.9f)
        } else {
            Toast.makeText(this, "Error al inicializar Text to Speech", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readNoteContent(note: Note, locale: Locale) {
        val translatedContent = if (locale.language == "en") {
            translateToEnglish(note.content)
        } else {
            note.content
        }

        textToSpeech.language = locale
        textToSpeech.speak(translatedContent, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun translateToEnglish(content: String): String {
        // Aquí puedes añadir una lógica de traducción básica.
        // Esto es solo un ejemplo; en un caso real podrías usar una API de traducción.
        return when (content) {
            "Hola, ¿cómo estás?" -> "Hello, how are you?"
            "¿Podrías ayudarme, por favor?" -> "Could you help me, please?"
            "¿Dónde está el baño?" -> "Where is the bathroom?"
            else -> content // Si no hay traducción, mantener el texto original.
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun NotesScreen(
    notesList: List<Note>,
    onAddNote: () -> Unit,
    onReadNote: (Note, Locale) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Inclusive Voice Notes App",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                modifier = Modifier
                    .statusBarsPadding()
                    .background(Color(0xFF030A25)),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF030A25),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNote,
                contentColor = Color.White,
                containerColor = Color(0xFF009688),
                modifier = Modifier
                    .size(120.dp)
                    .padding(16.dp)
                    .semantics { contentDescription = "Agregar nueva nota" }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFFFFF), Color(0xFF030A25))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (notesList.isEmpty()) {
                    Text(
                        text = "No hay notas disponibles.",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(notesList) { note ->
                            NoteCard(note = note, onReadNote = onReadNote)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NoteCard(note: Note, onReadNote: (Note, Locale) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F6186)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.content,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { onReadNote(note, Locale("es", "ES")) }) {
                Icon(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "Español")
            }
            IconButton(onClick = { onReadNote(note, Locale("en", "GB")) }) {
                Icon(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "English")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    IvnComposeTheme {
        NotesScreen(
            notesList = listOf(
                Note("Saludo", "Hola, ¿cómo estás?"),
                Note("Pedido de ayuda", "¿Podrías ayudarme, por favor?")
            ),
            onAddNote = {},
            onReadNote = { _, _ -> }
        )
    }
}
