package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"time"
)

func oracle(msgChan chan Message, responseChan chan Message, feedbackChan chan Message) {
	defer wg.Done()

	var n int = generateRandom(MAX)
	turn := 1

	for turn <= MAX_TURNS {
		// Invia il messaggio iniziale a tutti i giocatori
		for i := 0; i < N_PLAYERS; i++ {
			msg := Message{
				senderID: 0,
				content:  fmt.Sprintf("Turno %d", turn),
				turn:     turn,
			}
			fmt.Println("Oracolo invia:", msg.content)
			msgChan <- msg
			time.Sleep(100 * time.Microsecond)
		}

		var ended bool = false
		// Riceve risposte dai giocatori e invia feedback
		for i := 0; i < N_PLAYERS; i++ {
			response := <-responseChan
			fmt.Printf("Oracolo riceve risposta dal giocatore %d: %s\n", response.senderID, response.content)
			var num int
			var err error
			num, err = strconv.Atoi(response.content)
			if err != nil {
				fmt.Println("Errore durante la conversione:", err)
				return
			}
			var feedbackContent string = checkNumber(n, num)

			if ended {
				feedbackContent = "Stop"
			}
			feedback := Message{
				senderID: 0,
				content:  feedbackContent,
				turn:     turn,
			}
			fmt.Printf("Oracolo invia a giocatore %d: %s\n", response.senderID, feedback.content)
			feedbackChan <- feedback
			time.Sleep(1 * time.Second)

			// Se il numero è indovinato
			if feedbackContent == "Ok" {
				fmt.Println("Giocatore", response.senderID, "ha indovinato il numero!")
				ended = true

			}
		}
		if ended {
			close(msgChan)
			close(responseChan)
			close(feedbackChan)
			return
		}
		turn++
	}
}

func generateRandom(max int) int {
	var num = rand.Intn(max + 1)
	fmt.Println("Il numero random da indovinare è: ", num)
	return num
}

func checkNumber(n int, nToCheck int) string {
	if n > nToCheck {
		return "Troppo basso"
	} else if n < nToCheck {
		return "Troppo alto"
	} else {
		return "Ok"
	}
}
