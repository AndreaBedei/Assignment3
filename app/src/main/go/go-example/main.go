package main

func main() {
	var numAgents = 5
	var max = 100
	var channels []chan Msg
	//ch := spawnMyOracle("0")

	for i := 0; i < numAgents; i++ {
		ch := make(chan Msg)
		//agentId := fmt.Sprintf("agent-%d", i)
		channels = append(channels, ch)
	}

	spawnMyOracle("0", max, channels)

	for i := 0; i < numAgents; i++ {
		spawnMyPlayer(max, channels[i])
	}
}
