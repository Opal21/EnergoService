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

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import pl.energosystem.energoservice.model.User
import pl.energosystem.energoservice.model.service.AccountService

class AccountServiceImpl constructor(private val auth: FirebaseAuth) : AccountService {

  override val currentUserId: String
    get() = auth.currentUser?.uid.orEmpty()

  override val hasUser: Boolean
    get() = auth.currentUser != null

  override val currentUser: Flow<User>
    get() = callbackFlow {
      val listener =
        FirebaseAuth.AuthStateListener { auth ->
          this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User())
        }
      auth.addAuthStateListener(listener)
      awaitClose { auth.removeAuthStateListener(listener) }
    }

  override suspend fun authenticate(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).await()
  }

  override suspend fun sendRecoveryEmail(email: String) {
    auth.sendPasswordResetEmail(email).await()
  }

  override suspend fun linkAccount(email: String, password: String) {
    val credential = EmailAuthProvider.getCredential(email, password)
    auth.currentUser!!.linkWithCredential(credential).await()
  }

  override suspend fun deleteAccount() {
    auth.currentUser!!.delete().await()
  }

  override suspend fun signOut() {
    auth.signOut()
  }

  companion object {
    private const val LINK_ACCOUNT_TRACE = "linkAccount"
  }
}
