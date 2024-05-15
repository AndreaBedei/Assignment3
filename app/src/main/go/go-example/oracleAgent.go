package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"sync"
)

// Struttura dei messaggi che inviamo.
type Msg struct {
	content  string
}

var numToGuess int // Numero comune da indovinare, generato random.
var winner bool = false // Controlliamo se c'è un vincitore nel turno appena passato.
var turnSync sync.WaitGroup // Creiamo il concetto di attesa per turno.

// Per ciascun giocatore presente.
func MyOracle(ch chan Msg, id string) {
	ch <- Msg{content: "Start"} // Inviamo il messaggio che il giocatore può iniziare a giocare.
	attempt := <-ch // Attendiamo che il giocatore ci invii il numero proposto.
	numReceived, err := strconv.Atoi(attempt.content) // Lo convertiamo.
	if err != nil {
        fmt.Println("Errore durante la conversione della stringa in intero:", err)
        return
    }
	// In base al numero gli indichiamo se è basso, alto o è quello indovinato.
	if numReceived > numToGuess {
		ch <- Msg{content: "lower"}
	} else if numReceived < numToGuess{
		ch <- Msg{content: "greater"}
	} else {
		ch <- Msg{content: "winner"}
		winner = true // Impostiamo il flag al fine di comunicarlo ai giocatori.
	}
	defer turnSync.Done() // Diciamo che la seguente go routine è stata completata.
}

func spawnMyOracle(id string, max int, channels []chan Msg) {
	numToGuess = generateRandom(max) // Generiamo il numero random.
	for ; !winner ; { // Continuiamo fino a che non c'è un vincitore.
		turnSync.Add(len(channels)) // Aggiungiamo n elementi al gruppo di attesa quanti sono i palyer per gestire i turni.
		for i := 0; i < len(channels); i++ {
			go MyOracle(channels[i], id) // Mettiamo l'oracolo in ascolto per ciascun player che gioca in una go routine.
		}
		turnSync.Wait() // Aspettiamo che tutti i player abbiano giocato.
		for i := 0; i < len(channels); i++ {
			if(winner){ // Se nel turno c'è stato un vincitore lo comunichiamo a tutti.
				channels[i] <- Msg{content: "close"} // Messaggio di chiusura.
			} else {
				channels[i] <- Msg{content: "_"} // Messaggio di continuazione.
			}
		}
		fmt.Println("Fine del turno")
	}
}

func generateRandom(max int) int {
	var num = rand.Intn(max + 1)
	fmt.Println("Il numero random da indovinare è: ", num)
	return num
}
