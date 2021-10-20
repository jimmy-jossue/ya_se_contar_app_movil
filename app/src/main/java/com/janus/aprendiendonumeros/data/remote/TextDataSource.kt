package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.core.TextProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TextDataSource {

    companion object {
        private const val PATH: String = "/vocabularies"
        private const val FIGURES: String = "figures"
        private const val SIGN_IN_SECTION: String = "sign_in_section"
        private const val SIGN_UP_SECTION: String = "sign_up_section"
        private const val SETTINGS_SECTION: String = "settings_section"
        private const val ALERT_DIALOGS: String = "alert_dialogs"
        private const val EXERCISE_KNOW_NUMBERS: String = "exercise_know_numbers"
        private const val EXERCISE_SELECT_AND_COUNT: String = "exercise_select_and_count"
        private const val EXERCISE_MOVE_AND_COUNT: String = "exercise_move_and_count"
        private const val EXERCISE_HOW_MANY: String = "exercise_how_many"
        private const val EXERCISE_LESS_OR_MORE: String = "exercise_less_or_more"
        private const val EXERCISE_ORDER_AND_COUNT: String = "exercise_order_and_count"
        private const val OTHER_PHRASES: String = "other_phrases"
    }

    suspend fun get(language: String) {
        withContext(Dispatchers.IO) {
            val textCollectionReference = FirebaseFirestore
                .getInstance()
                .collection(PATH)

            val textDocument = textCollectionReference.document(language).get().await()

            textDocument.data.let { maps ->
                TextProvider.figures = maps?.get(FIGURES) as Map<String, String>
                TextProvider.signInSection = maps?.get(SIGN_IN_SECTION) as Map<String, String>
                TextProvider.signUpSection = maps?.get(SIGN_UP_SECTION) as Map<String, String>
                TextProvider.settingsSection = maps?.get(SETTINGS_SECTION) as Map<String, String>
                TextProvider.alertDialogs = maps?.get(ALERT_DIALOGS) as Map<String, String>
                TextProvider.exerciseKnowNumbers =
                    maps?.get(EXERCISE_KNOW_NUMBERS) as Map<String, String>
                TextProvider.exerciseSelectAndCount =
                    maps?.get(EXERCISE_SELECT_AND_COUNT) as Map<String, String>
                TextProvider.exerciseMoveAndCount =
                    maps?.get(EXERCISE_MOVE_AND_COUNT) as Map<String, String>
                TextProvider.exerciseHowMany = maps?.get(EXERCISE_HOW_MANY) as Map<String, String>
                TextProvider.exerciseLessOrMore =
                    maps?.get(EXERCISE_LESS_OR_MORE) as Map<String, String>
                TextProvider.exerciseOrderAndCount =
                    maps?.get(EXERCISE_ORDER_AND_COUNT) as Map<String, String>
                TextProvider.otherPhrases = maps?.get(OTHER_PHRASES) as Map<String, String>
            }
        }
    }
}