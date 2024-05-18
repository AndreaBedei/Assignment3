package main

import (
	"fmt"
	"math/rand"
)

func player(id int, msgChan chan Message, responseChan chan Message, feedbackChan chan Message) {
	defer wg.Done()
	var min int = 0
	var max int = MAX
	var ended bool = false
	for msg := range msgChan {
		fmt.Printf("Giocatore %d ha ricevuto: %s\n", id, msg.content)

		var numGenerated int = generateRandomWithMin(max, min)
		// Risponde all'oracolo
		reply := Message{
			senderID: id,
			content:  fmt.Sprintf("%d", numGenerated),
		}
		responseChan <- reply

		// Attende il feedback dall'oracolo
		feedback := <-feedbackChan
		fmt.Printf("Giocatore %d ha ricevuto feedback: %s\n", id, feedback.content)

		if feedback.content == "Troppo alto" {
			max = numGenerated - 1
		} else if feedback.content == "Troppo basso" {
			min = numGenerated + 1
		} else {
			ended = true
		}
		if ended {
			break
		}
	}
}

func generateRandomWithMin(max int, min int) int {
	var num = rand.Intn(max-min+1) + min
	return num
}
