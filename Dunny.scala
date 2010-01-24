package org.chilon.dunny

import java.io.DataOutputStream

object Dunny {
    val BIT_RATE = 44100
    val LENGTH = 4

    def main(args: Array[String]) {
        var r = new Random
        var b = new DataOutputStream(System.out)
        for (i <- 1 to BIT_RATE * LENGTH) {
            val v = (r.nextFloat * 2) - 1
            b.writeFloat(v)
        }
    }
}
