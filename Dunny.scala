package org.chilon.dunny

import java.io.DataOutputStream

class Sample {
    var time:Double = 0

    def sin(frequence:Double) = Math.sin(frequence * time)*2-1
    def saw(frequence:Double) = ((frequence * time* (1 / Math.Pi))%2)-1

    def increment:Double = {
        time+=1
        return time
    }
}

object Dunny {
    val BIT_RATE = 44100
    val LENGTH = 16
    def main(args: Array[String]) {
        var b = new DataOutputStream(System.out)
        var r = new Random
        var s = new Sample
        while (s.increment < BIT_RATE * LENGTH) {
            val mix = s.sin(chromatic(7)) + s.saw(chromatic(0))// + r.nextDouble
            b.writeDouble(mix*0.5)
        }
    }

    def chromatic(pitch:Double) = Math.pow(2, (pitch/12)-6)

}
