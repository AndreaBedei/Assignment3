package main

import (
	"fmt"
	"math/rand"
)

type Msg struct {
	content  string
	senderId string
}

var num int

func MyOracle(ch chan Msg, id string) {
	msg := Msg{content: "Start", senderId: id}
	ch <- msg

	message := <-ch
	fmt.Println(message)
	defer wg.Done()
}

func spawnMyOracle(id string, max int, channels []chan Msg) {
	num = generateRandom(max)
	for i := 0; i < len(channels); i++ {
		wg.Add(1)
		go MyOracle(channels[i], id)
	}
	//fmt.Print("Ended")
}

func generateRandom(max int) int {
	var num = rand.Intn(max + 1)
	//fmt.Println(num)
	return num
}
