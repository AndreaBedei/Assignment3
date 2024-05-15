package main

import "sync"

var wg sync.WaitGroup

func main() {
	var numAgents = 5
	var max = 100000000
	var channels []chan Msg

	for i := 0; i < numAgents; i++ {
		ch := make(chan Msg)
		channels = append(channels, ch)
	}

	go spawnMyOracle("0", max, channels)
	
	for i := 0; i < numAgents; i++ {
		spawnMyPlayer(max, channels[i], i)
	}
	
	wg.Wait()
}
