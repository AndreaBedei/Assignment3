package main

import (
	"fmt"
	"math/rand"
	"strconv"
)

func spawnMyPlayer(maxR int, ch chan Msg, i int) {
	var id = i // Id del giocatore.
	var max int = maxR
	wg.Add(1) // Aggiungiamo un elemento al gruppo di attesa.
	go guessNumber(ch, id, max) // Creiamo una go routine per indovinare il numero da parte del giocatore.
}

func guessNumber(ch chan Msg, id int, maxV int) {
	var continueIter = true
	var min int = 0
	var max int = maxV
	for ; continueIter ;  { // Continuo finchè qualcuno non ha indovinato.
		message := <-ch // Aspetto il messaggio di inizio.
		if message.content == "Start" { 
			var num = rand.Intn(max-min+1) + min // Aggiorno il range ad ogni iterazione.
			ch <- Msg{content: strconv.Itoa(num)} // invio il numero random del tentativo.
			message := <-ch // Attendo la risposta dall'oracolo.
			if message.content == "lower"{
				fmt.Println("Il player ", id, " ha tentato col numero: ", num, " ma esso è troppo alto!")
				max = num - 1
			} else if message.content == "greater"{
				fmt.Println("Il player ", id, " ha tentato col numero: ", num, " ma esso è troppo basso!")
				min = num + 1
			} else {
				fmt.Println("Il player ", id, " ha indovinato! Col numero: ", num)
			}
			msg := <-ch // Attendiamo di sapere se continuare o fermarci dall'oracolo, nel caso qualcuno avesse indovinato.
			if(msg.content == "close"){
				continueIter = false // Dico di concludere la goRoutine.
			}
		}
	}
	defer wg.Done() // Segnalo il completamento.
}
