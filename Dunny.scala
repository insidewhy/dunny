package org.chilon.dunny

import java.io.DataOutputStream

class Sample (b:Double){
    var time:Double = 0
    val bitrate = b

    def sin(frequence:Double) = Math.sin(frequence * time*2*Math.Pi)
    def saw(frequence:Double) = ((frequence * time)%1)*2-1
    def sqr(frequence:Double) = if ((frequence*time)%1 < 0.5) 1 else -1
    def isin(frequence:Double) = -sin(frequence)
    def isaw(frequence:Double) = -saw(frequence)
    def isqr(frequence:Double) = -sqr(frequence)
    def increment:Double = {
        time+=1/bitrate
        return time
    }
}
class Sequence(s:Array[Double], r:Double){
    val sequence = s
    val rate = r

    def discrete(time:Double) = sequence((time*rate).toInt%sequence.length)

    def linear(time:Double):Double = {
        var position = sequence((time*rate).toInt % sequence.length)
        var next = sequence((time*rate + 1).toInt % sequence.length)
        var ratio = (time * rate) % 1

        return position*(1-ratio) + next*ratio
    }
}

object Dunny {
    val BITRATE = 44100
    val LENGTH = 256
    def main(args: Array[String]) {
        var b = new DataOutputStream(System.out)
        var s = new Sample(BITRATE)
        var notes = new Sequence(Array(0, 5, 7, 5, 0, 0, 7, 12, 19), 4)
        var notes2 = new Sequence(Array(0, 7, 12, 0), 1)
        var notes3 = new Sequence(Array(0, 12, 19, 0, 7), 2)
        var key = new Sequence(Array(0, 3, -2, 1), 0.25)
        var prev:Double = 0

        while (s.increment < LENGTH) {
            var mix:Double = 0
            mix += (s.saw(chromatic(notes.discrete(s.time)-12+key.discrete(s.time))) + 
                    s.saw(chromatic(notes.discrete(s.time) -12 + 0.05+key.discrete(s.time))))*
                        (s.saw(2)*0.5+0.5)
            mix += (s.saw(chromatic(notes2.discrete(s.time)+key.discrete(s.time))) + 
                    s.sqr(chromatic(notes3.discrete(s.time)+0.1+key.discrete(s.time)))*0.3)*
                        (s.isaw(8)*0.5 + 0.5)
            mix += (s.sqr(chromatic(notes3.discrete(s.time)+12+key.discrete(s.time)))*0.3 +
                    s.sqr(chromatic(notes3.discrete(s.time)+11.9+key.discrete(s.time)))*0.3)*
                        (s.isaw(1)*0.5 + 0.5)
            // TODO: correct phase with prev
            b.writeDouble(mix*0.25)
            b.flush()
            prev = s.time
        }
    }

    def chromatic(pitch:Double) = Math.pow(2, (pitch/12)+7)

}
