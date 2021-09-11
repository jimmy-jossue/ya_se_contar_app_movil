package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.ui.fragment.menu.MenuItemView
import kotlinx.coroutines.tasks.await

class ExerciseDataSource {

    suspend fun createExercises(userId: String) {
        val map = getImages()

        val userDocumentRef = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)
            .document(userId)

        val exerciseCollectionRef = userDocumentRef.collection(Exercise.PATH_EXERCISES)

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
                Exercise.INDEX_IN_MENU to index,
                Exercise.NAME to keyName,
                Exercise.IMAGE to value,
                Exercise.STATUS to status,
                Exercise.POSITION to position,
            )
            exerciseCollectionRef.document(keyName).set(data).await()
            index++
        }
    }

    private suspend fun getImages(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        val exercisesCollectionRef = FirebaseFirestore
            .getInstance()
            .collection("/defaults")
            .document("/default_exercises_images")

        val data = exercisesCollectionRef.get().await()

        map[Exercise.NAME_KNOW_NUMBERS] = data.get(Exercise.NAME_KNOW_NUMBERS).toString()
        map[Exercise.NAME_SELECT_AND_COUNT] = data.get(Exercise.NAME_SELECT_AND_COUNT).toString()
        map[Exercise.NAME_MOVE_AND_COUNT] = data.get(Exercise.NAME_MOVE_AND_COUNT).toString()
        map[Exercise.NAME_HOW_MANY] = data.get(Exercise.NAME_HOW_MANY).toString()
        map[Exercise.NAME_LESS_OR_MORE] = data.get(Exercise.NAME_LESS_OR_MORE).toString()
        map[Exercise.NAME_ORDER_AND_COUNT] = data.get(Exercise.NAME_ORDER_AND_COUNT).toString()
        //map[Exercise.NAME_LOCKED] = data.get(Exercise.NAME_LOCKED).toString()

        return map
    }

    suspend fun getExercise(userId: String, nameExercise: String): Exercise {
        val exercisesCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)
            .document(userId)
            .collection(Exercise.PATH_EXERCISES)

        val exerciseDocumentRef = exercisesCollectionReference.document(nameExercise).get().await()

        return Exercise(
            indexInMenu = exerciseDocumentRef.data?.get(Exercise.INDEX_IN_MENU).toString().toInt(),
            name = exerciseDocumentRef.data?.get(Exercise.NAME).toString(),
            image = exerciseDocumentRef.data?.get(Exercise.IMAGE).toString(),
            status = exerciseDocumentRef.data?.get(Exercise.STATUS).toString().toInt(),
            position = exerciseDocumentRef.data?.get(Exercise.POSITION).toString().toInt()
        )
    }

    suspend fun getAllExercises(userId: String): List<Exercise> {
        val list = mutableListOf<Exercise>()

        val exercisesCollectionRef = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)
            .document(userId)
            .collection(Exercise.PATH_EXERCISES)
            .orderBy(Exercise.INDEX_IN_MENU)
            .get()
            .await()


        for (document in exercisesCollectionRef.documents) {
            val exercise = Exercise(
                indexInMenu = document.data?.get(Exercise.INDEX_IN_MENU).toString().toInt(),
                name = document.data?.get(Exercise.NAME).toString(),
                image = document.data?.get(Exercise.IMAGE).toString(),
                status = document.data?.get(Exercise.STATUS).toString().toInt(),
                position = document.data?.get(Exercise.POSITION).toString().toInt()
            )
            list.add(exercise)
        }

        return list
    }

    suspend fun setExercise(userId: String, exercise: Exercise) {
        val exercisesCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)
            .document(userId)
            .collection(Exercise.PATH_EXERCISES)

        val data = hashMapOf(
            Exercise.INDEX_IN_MENU to exercise.indexInMenu,
            Exercise.NAME to exercise.name,
            Exercise.IMAGE to exercise.image,
            Exercise.STATUS to exercise.status,
            Exercise.POSITION to exercise.position
        )

        exercisesCollectionReference
            .document(exercise.name)
            .set(data)
            .await()
    }

    suspend fun updateExercise(userId: String, nameExercise: String, data: Map<String, Any>) {
        val usersDocumentRef = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)
            .document(userId)

        val exerciseDocumentRef = usersDocumentRef
            .collection(Exercise.PATH_EXERCISES)
            .document(nameExercise)

        exerciseDocumentRef.update(data).await()
    }
}