package ir.adicom.mymoney

import kotlinx.coroutines.*
import org.junit.Test

class CoroutineTest {

    @Test
    fun testCancellation() = runBlocking {

        println("1")
        val job = launch {
            println("3")
            try {
                println("4")
                delay(5000)
                println("Done")
            } finally {
                println("8")
                println("Finally")
            }
        }

        println("2")
        delay(500)
        println("5")
        job.cancel()
        println("6")

        try {
            println("7")
            job.join()
            println("Joined")
        } catch (e: CancellationException) {
            println("Join cancelled")
        }

        println("After")
    }

    @Test
    fun testCancellation02() = runBlocking {
        val job = launch {
            try {
                repeat(5) { i ->
                    println("Work $i")
                    delay(1000)
                }
            } finally {
                println("Cleanup")
            }
        }

        delay(1500)
        job.cancelAndJoin()

        println("After")
    }

    @Test
    fun testCancellation03() = runBlocking {
        val job = launch {
            try {
                repeat(5) { i ->
                    println("Work $i")
                    delay(1000)
                }
            } finally {
                println("Cleanup")
            }
        }

        delay(1500)
        job.cancel()

        println("After cancel")

        job.join()

        println("After join")
    }

    @Test
    fun testCancellation04() = runBlocking {
        val job = launch {
            try {
                delay(5000)
                println("Done")
            } finally {
                println("Finally")
                delay(1000)
                println("Cleanup done")
            }
        }

        delay(500)
        job.cancelAndJoin()

        println("After")
    }

    @Test
    fun testCancellation05() = runBlocking {
        val job = launch {
            repeat(5) { i ->
                println("Before $i")

                ensureActive()

                println("After $i")

                delay(1000)
            }
        }

        delay(1500)
        job.cancelAndJoin()

        println("Done")
    }
}