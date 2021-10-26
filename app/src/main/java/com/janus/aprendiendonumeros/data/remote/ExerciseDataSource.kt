package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.ui.fragment.menu.MenuItemView
import kotlinx.coroutines.tasks.await

class ExerciseDataSource {

    companion object {
        const val PATH: String = "/exercises"
        const val INDEX_IN_MENU: String = "indexInMenu"
        const val NAME: String = "name"
        const val IMAGE: String = "image"
        const val STATUS: String = "status"
        const val POSITION: String = "position"
    }

    suspend fun createExercises(userId: String) {
        val map = getImages()

        val userDocumentReference = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)
            .document(userId)

        val exerciseCollectionRef = userDocumentReference.collection(PATH)

        var index = 0
        map.forEach { (keyName, value) ->

            val status: Int = when (keyName) {
                Exercise.NAME_KNOW_NUMBERS -> MenuItemView.STATUS_EMPTY
                else -> MenuItemView.STATUS_LOCKED
            }

            val position: Int = when (keyName) {
                Exercise.NAME_KNOW_NUMBERS -> MenuItemView.POSITION_START
                Exercise.NAME_ORDER_AND_COUNT -> MenuItemView.POSITION_END
                else -> MenuItemView.POSITION_MIDDLE
            }

            val data = hashMapOf(
                INDEX_IN_MENU to index,
                NAME to keyName,
                IMAGE to value,
                STATUS to status,
                POSITION to position,
            )
            exerciseCollectionRef.document(keyName).set(data).await()
            index++
        }
    }

    private suspend fun getImages(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        val exercisesCollectionReference = FirebaseFirestore
            .getInstance()
            .collection("/defaults")
            .document("/default_exercises_images")

        val data = exercisesCollectionReference
            .get()
            .await()

        map[Exercise.NAME_KNOW_NUMBERS] = data.get(Exercise.NAME_KNOW_NUMBERS).toString()
        map[Exercise.NAME_SELECT_AND_COUNT] = data.get(Exercise.NAME_SELECT_AND_COUNT).toString()
        map[Exercise.NAME_MOVE_AND_COUNT] = data.get(Exercise.NAME_MOVE_AND_COUNT).toString()
        map[Exercise.NAME_HOW_MANY] = data.get(Exercise.NAME_HOW_MANY).toString()
        map[Exercise.NAME_LESS_OR_MORE] = data.get(Exercise.NAME_LESS_OR_MORE).toString()
        map[Exercise.NAME_ORDER_AND_COUNT] = data.get(Exercise.NAME_ORDER_AND_COUNT).toString()

        return map
    }

    suspend fun getExercise(userId: String, nameExercise: String): Exercise {
        val exercisesCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)
            .document(userId)
            .collection(PATH)

        val exerciseDocumentRef = exercisesCollectionReference
            .document(nameExercise)
            .get()
            .await()

        return Exercise(
            indexInMenu = exerciseDocumentRef.data?.get(INDEX_IN_MENU).toString().toInt(),
            name = exerciseDocumentRef.data?.get(NAME).toString(),
            image = exerciseDocumentRef.data?.get(IMAGE).toString(),
            status = exerciseDocumentRef.data?.get(STATUS).toString().toInt(),
            position = exerciseDocumentRef.data?.get(POSITION).toString().toInt()
        )
    }

    suspend fun getAllExercises(userId: String): List<Exercise> {
        val list = mutableListOf<Exercise>()

        val exercisesCollectionRef = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)
            .document(userId)
            .collection(PATH)
            .orderBy(INDEX_IN_MENU)
            .get()
            .await()


        for (document in exercisesCollectionRef.documents) {
            val exercise = Exercise(
                indexInMenu = document.data?.get(INDEX_IN_MENU).toString().toInt(),
                name = document.data?.get(NAME).toString(),
                image = document.data?.get(IMAGE).toString(),
                status = document.data?.get(STATUS).toString().toInt(),
                position = document.data?.get(POSITION).toString().toInt()
            )
            list.add(exercise)
        }

        return list
    }

    suspend fun setExercise(userId: String, exercise: Exercise) {
        val exercisesCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)
            .document(userId)
            .collection(PATH)

        val data = hashMapOf(
            INDEX_IN_MENU to exercise.indexInMenu,
            NAME to exercise.name,
            IMAGE to exercise.image,
            STATUS to exercise.status,
            POSITION to exercise.position
        )

        exercisesCollectionReference
            .document(exercise.name)
            .set(data)
            .await()
    }

    suspend fun updateExercise(userId: String, nameExercise: String, data: Map<String, Any>) {
        val usersDocumentRef = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)
            .document(userId)

        val exerciseDocumentRef = usersDocumentRef
            .collection(PATH)
            .document(nameExercise)

        exerciseDocumentRef.update(data).await()
    }
}