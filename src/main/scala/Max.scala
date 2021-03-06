package rosetta

import Chisel._

class Max(nums_in: Int, dataWidth: Int) extends RosettaAccelerator {
    def Compare(a:UInt, b:UInt):UInt = {
        Mux(a > b, a, b)
    }

    val numMemPorts = 0
    val io = new RosettaAcceleratorIF(numMemPorts) {
        val nums = Vec.fill(nums_in){UInt(INPUT, dataWidth)}

        val dataOut = UInt(OUTPUT, dataWidth)
    }

    if(dataWidth == 1) {
        io.dataOut := io.nums.reduceLeft(_ | _)
    } else {
        io.dataOut := io.nums.reduceLeft(Compare)
    }
}

class MaxTests(c: Max) extends Tester(c) {
    val input = Array[BigInt](1,1,0,0)
    poke(c.io.nums, input)
    expect(c.io.dataOut, input.reduceLeft(_ max _))
}

