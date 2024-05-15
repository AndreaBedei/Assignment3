package main

import (
	"fmt"
	"math/rand"
	"strconv"
)

var continueIter = true

func spawnMyPlayer(maxR int, ch chan Msg, i int) {
	var id = i
	var max int = maxR
	wg.Add(1)
	go guessNumber(ch, id, max)
}

func guessNumber(ch chan Msg, id int, maxV int) {
	var min int = 0
	var max int = maxV
	for ; continueIter ;  {
		message := <-ch
		if message.content == "Start" {
			var num = rand.Intn(max-min+1) + min
			ch <- Msg{content: strconv.Itoa(num)}
			message := <-ch
			if message.content == "lower"{
				fmt.Println("Il player ", id, " ha tentato col numero: ", num, " ma esso è troppo alto!")
				max = num - 1
			} else if message.content == "greater"{
				fmt.Println("Il player ", id, " ha tentato col numero: ", num, " ma esso è troppo basso!")
				min = num + 1
			} else {
				fmt.Println("Il player ", id, " ha indovinato! Col numero: ", num)
			}
			msg := <-ch
			if(msg.content == "close"){
				continueIter = false
			}
		}
	}
	defer wg.Done()
}
