/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package pl.energosystem.energoservice.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import pl.energosystem.energoservice.model.Task
import pl.energosystem.energoservice.model.service.AccountService
import pl.energosystem.energoservice.model.service.TaskStorageService

class TaskStorageServiceImpl
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) :
  TaskStorageService {

  @OptIn(ExperimentalCoroutinesApi::class)
  override val tasks: Flow<List<Task>>
    get() = auth.currentUser.flatMapLatest { user ->
      firestore.collection(TASK_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
    }

  override suspend fun getTask(taskId: String): Task? =
    firestore.collection(TASK_COLLECTION).document(taskId).get().await().toObject()

  override suspend fun save(task: Task): String {
      val taskWithUserId = task.copy(userId = auth.currentUserId)
      return firestore.collection(TASK_COLLECTION).add(taskWithUserId).await().id
    }

  override suspend fun update(task: Task) {
      firestore.collection(TASK_COLLECTION).document(task.id).set(task).await()
    }

  override suspend fun delete(taskId: String) {
    firestore.collection(TASK_COLLECTION).document(taskId).delete().await()
  }

  companion object {
    private const val USER_ID_FIELD = "userId"
    private const val TASK_COLLECTION = "tasks"
    private const val SAVE_TASK_TRACE = "saveTask"
    private const val UPDATE_TASK_TRACE = "updateTask"
  }
}

