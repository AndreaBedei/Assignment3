package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"sync"
)

type Msg struct {
	content  string
}

var numToGuess int
var winner bool = false
var turnSync sync.WaitGroup

func MyOracle(ch chan Msg, id string) {
	ch <- Msg{content: "Start"}
	attempt := <-ch
	numReceived, err := strconv.Atoi(attempt.content)
	if err != nil {
        fmt.Println("Errore durante la conversione della stringa in intero:", err)
        return
    }

	if numReceived > numToGuess {
		ch <- Msg{content: "lower"}
	} else if numReceived < numToGuess{
		ch <- Msg{content: "greater"}
	} else {
		ch <- Msg{content: "winner"}
		winner = true
	}
	defer turnSync.Done()
	defer wg.Done()
}

func spawnMyOracle(id string, max int, channels []chan Msg) {
	numToGuess = generateRandom(max)
	for ; !winner ; {
		turnSync.Add(len(channels))
		for i := 0; i < len(channels); i++ {
			wg.Add(1)
			go MyOracle(channels[i], id)
		}
		turnSync.Wait()
		for i := 0; i < len(channels); i++ {
			if(winner){
				channels[i] <- Msg{content: "close"}
			} else {
				channels[i] <- Msg{content: "continue"}
			}
		}
		fmt.Println("Fine turno")
	}
}

func generateRandom(max int) int {
	var num = rand.Intn(max + 1)
	fmt.Println("Il numero random da indovinare è: ", num)
	return num
}
