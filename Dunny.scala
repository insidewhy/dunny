package org.chilon.dunny

import java.io.DataOutputStream

class Sample (b:Double){
    var time:Double = 0
    val bitrate = b

    def sin(frequence:Double) = Math.sin(frequence * time*2*Math.Pi)
    def saw(frequence:Double) = (frequence * time)%2-1
    def sqr(frequence:Double) = if ((frequence*time)%2 < 1) 1 else -1
    def increment:Double = {
        time+=1/bitrate
        return time
    }
}
class Sequence(s:Array[Double], r:Double){
    var sequence = s
    var rate = r

    def value(time:Double) = sequence((time*rate).toInt%sequence.length)

    def linear(time:Double):Double = {
        var position = time*rate
        var floor = Math.floor(position).toInt%sequence.length
        var ceil = (floor+1).toInt%sequence.length
        var ratio = position%1
        return sequence(floor)*(1-ratio) + sequence(ceil)*ratio
    }
}

object Dunny {
    val BITRATE = 44100
    val LENGTH = 16
    def main(args: Array[String]) {
        var b = new DataOutputStream(System.out)
        var r = new Random
        var s = new Sample(BITRATE)
        var notes = new Sequence(Array(0, 7, 12, 0), 1)
        var notes3 = new Sequence(Array(0, 5, 7, 5, 0, 0, 7, 12), 3)
        while (s.increment < LENGTH) {
            //var pitch = s.sqr(notes.value(s.time))
            var pitch2 = s.sqr(0.5)
            val mix = 
                s.sqr(chromatic(notes.linear(s.time))) +
                s.sqr(chromatic(notes3.value(s.time)+12))
            b.writeDouble(mix*0.5)
        }
    }

    def chromatic(pitch:Double) = Math.pow(2, (pitch/12)+7)

}
