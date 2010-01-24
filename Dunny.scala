package org.chilon.dunny

import java.io.DataOutputStream

class Sample (b:Double){
    var time:Double = 0
    val bitrate = b

    def sin(frequence:Double) = Math.sin(frequence * time*2*Math.Pi)
    def saw(frequence:Double) = (frequence * time)%2-1
    def sqr(frequence:Double) = if ((frequence*time)%1 < 0.5) 1 else -1
    def increment:Double = {
        time+=1/bitrate
        return time
    }
}
class Sequence(s:Array[Double], r:Double){
    val sequence = s
    val rate = r

    def discreet(time:Double) = sequence((time*rate).toInt%sequence.length)

    def linear(time:Double):Double = {
        var position = sequence((time*rate).toInt % sequence.length)
        var next = sequence((time*rate + 1).toInt % sequence.length)
        var ratio = (time * rate) % 1

        return position*(1-ratio) + next*ratio
    }
}

object Dunny {
    val BITRATE = 44100
    val LENGTH = 16
    def main(args: Array[String]) {
        var b = new DataOutputStream(System.out)
        var s = new Sample(BITRATE)
        var notes = new Sequence(Array(0, 7, 12, 0), 3)
        var notes3 = new Sequence(Array(0, 5, 7, 5, 0, 0, 7, 12), 3)
        var prev:Double = 0

        while (s.increment < LENGTH) {
            val mix = 
                s.saw(chromatic(notes.linear(s.time))) +
                s.sqr(chromatic(notes3.discreet(s.time)+12))

            // TODO: correct phase with prev
            
            b.writeDouble(mix*0.5)
            prev = s.time
        }
    }

    def chromatic(pitch:Double) = Math.pow(2, (pitch/12)+7)

}
