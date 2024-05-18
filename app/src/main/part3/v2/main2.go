package main

import (
	"fmt"
	"sync"
)

// Struttura per i messaggi
type Message struct {
	senderID int
	content  string
	turn     int
}

var wg sync.WaitGroup
var N_PLAYERS = 5
var MAX = 100
var MAX_TURNS = 10

func main() {
	msgChan := make(chan Message)
	responseChan := make(chan Message)
	feedbackChan := make(chan Message)

	wg.Add(1)
	go oracle(msgChan, responseChan, feedbackChan)

	for i := 1; i <= N_PLAYERS; i++ {
		wg.Add(1)
		go player(i, msgChan, responseChan, feedbackChan)
	}

	wg.Wait()
	fmt.Println("Tutti i messaggi sono stati processati e tutte le risposte ricevute.")
}
