package com.gz.kidsafe.policy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ParentalGate(
    onSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    val challenge = remember { ParentChallenge.create() }
    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Parental Gate") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("For parents: solve this before opening parent area.")
                Text(challenge.prompt)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Answer") },
                    singleLine = true
                )
                error?.let { Text(it) }
            }
        },
        confirmButton = {
            Button(onClick = {
                val valid = ParentGateValidator.validate(input, challenge.answer)
                if (valid) {
                    onSuccess()
                } else {
                    error = "Incorrect answer. Parent access denied."
                }
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class ParentChallenge(val prompt: String, val answer: Int) {
    companion object {
        fun create(): ParentChallenge {
            val a = (21..49).random()
            val b = (11..29).random()
            return ParentChallenge(
                prompt = "What is $a + $b?",
                answer = a + b
            )
        }
    }
}

object ParentGateValidator {
    fun validate(input: String, expectedAnswer: Int): Boolean {
        return input.trim().toIntOrNull() == expectedAnswer
    }
}
