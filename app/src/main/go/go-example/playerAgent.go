package main

import (
	"math/rand"
	"strconv"
)

var max int
var min int = 0

func spawnMyPlayer(maxR int, ch chan Msg) {
	max = maxR
	message := <-ch

	if message.content == "Start" {
		wg.Add(1)
		go guessNumber(ch)
	}

}

func guessNumber(ch chan Msg) {
	//fmt.Print("spawnMyplayer")

	var num = rand.Intn(max-min) + min
	//fmt.Println(num)
	ch <- Msg{content: strconv.Itoa(num), senderId: "0"}
	//fmt.Println("muore")
	defer wg.Done()
}
