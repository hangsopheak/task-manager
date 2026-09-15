package kh.edu.rupp.taskmanagement.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await

// the failures a user can actually cause, in words the screen can show
sealed interface AuthError {
    data object WeakPassword : AuthError
    data object InvalidCredentials : AuthError
    data object UserCollision : AuthError
    data object Network : AuthError
    data object Other : AuthError
}

sealed interface AuthOutcome {
    data object Success : AuthOutcome
    data class Failure(val error: AuthError) : AuthOutcome
}

// the only file that knows Firebase Auth exists
class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    val isSignedIn: Boolean
        get() = auth.currentUser != null

    val userEmail: String
        get() = auth.currentUser?.email ?: ""

    suspend fun signIn(email: String, password: String): AuthOutcome =
        catchAuth { auth.signInWithEmailAndPassword(email, password).await() }

    suspend fun createAccount(email: String, password: String): AuthOutcome =
        catchAuth { auth.createUserWithEmailAndPassword(email, password).await() }

    fun signOut() {
        auth.signOut()
    }

    // weak password is a subclass of invalid credentials, so it has to be caught first
    private inline fun catchAuth(call: () -> Unit): AuthOutcome = try {
        call()
        AuthOutcome.Success
    } catch (e: FirebaseAuthWeakPasswordException) {
        AuthOutcome.Failure(AuthError.WeakPassword)
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AuthOutcome.Failure(AuthError.InvalidCredentials)
    } catch (e: FirebaseAuthUserCollisionException) {
        AuthOutcome.Failure(AuthError.UserCollision)
    } catch (e: FirebaseNetworkException) {
        AuthOutcome.Failure(AuthError.Network)
    } catch (e: Exception) {
        AuthOutcome.Failure(AuthError.Other)
    }
}
