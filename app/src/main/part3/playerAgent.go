package main

import (
	"fmt"
	"math/rand"
	"strconv"
)

func spawnMyPlayer(maxR int, ch chan Msg, i int, publiChannel chan Msg, resultChannel chan Msg) {
	var id = i // Id del giocatore.
	var max int = maxR
	wg.Add(1) // Aggiungiamo un elemento al gruppo di attesa.
	go guessNumber(ch, id, max, publiChannel, resultChannel) // Creiamo una go routine per indovinare il numero da parte del giocatore.
}

func guessNumber(syncChannel chan Msg, id int, maxV int, channelPublic chan Msg, sendResultChannel chan Msg) {
	var min int = 0
	var max int = maxV
	for (true) { // Continuo finchè qualcuno non ha indovinato.
		message := <-channelPublic // Aspetto il messaggio di inizio.
		if message.content == "Start" { 
			var num = rand.Intn(max-min+1) + min // Aggiorno il range ad ogni iterazione.
			sendResultChannel <- Msg{content: strconv.Itoa(num), id: id} // invio il numero random del tentativo sul canale pubblico per non determinismo.
			message := <-channelPublic // Attendo la risposta dall'oracolo.
			if message.content == "lower"{
				fmt.Println("Il player ", id, " ha tentato col numero: ", num, " ma esso è troppo alto!")
				max = num - 1
			} else if message.content == "greater"{
				fmt.Println("Il player ", id, " ha tentato col numero: ", num, " ma esso è troppo basso!")
				min = num + 1
			} else if message.content == "winnerNotMe" { // Accade quando il numero è corretto ma è stato gestito dopo a un altro messaggio di vittoria.
				fmt.Println("Il player ", id, " ha indovinato ma è arrivato dopo, quindi non ha vinto per poco :-(")
			} else { // Messaggio di vittoria.
				fmt.Println("Il player ", id, " ha indovinato! Col numero: ", num)
			}
			syncChannel <- Msg{content: "Finito turno", id: id} // Messaggio per la sincronizzazione per la fine di un turno.
		} else if message.content == "finish"{
			// Ultimo messaggio per concludere il gioco dichiarando chi ha vinto e chi ha perso.
			if(message.id == id){
				fmt.Println("L'utente ", id, " ha vinto!!!")
			} else {
				fmt.Println("L'utente ", id, " ha perso.")
			}
			break
		}
	}
	defer wg.Done() // Segnalo il completamento.
}
