package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class InstructionHowManyDataSource {

    companion object {
        private const val PATH: String = "/instructions"
    }

    suspend fun get(exerciseName: String): Map<String, String> {
        var map: Map<String, String>

        withContext(Dispatchers.IO) {
            val instructionCollectionReference = FirebaseFirestore
                .getInstance()
                .collection(PATH)

            val instructionDocument = instructionCollectionReference.document(exerciseName).get().await()

            map = instructionDocument.data?.get("urlQuestionsAudio") as? Map<String, String> ?: emptyMap()
        }

        return map
    }
}