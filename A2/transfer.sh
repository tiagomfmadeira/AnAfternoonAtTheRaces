gr="GeneralRepository"
bc="BettingCenter"
cc="ControlCenter"
pd="Paddock"
rt="RaceTrack"
st="Stable"
b="Broker"
hj="HorseJockey"
s="Spectator"

scp -r $gr/out/artifacts/$gr\_jar/$gr.jar sd0406@l040101-ws01.ua.pt:/home/sd0406/src

scp -r $bc/out/artifacts/$bc\_jar/$bc.jar sd0406@l040101-ws02.ua.pt:/home/sd0406/src

scp -r $cc/out/artifacts/$cc\_jar/$cc.jar sd0406@l040101-ws03.ua.pt:/home/sd0406/src

scp -r $pd/out/artifacts/$pd\_jar/$pd.jar sd0406@l040101-ws04.ua.pt:/home/sd0406/src

scp -r $rt/out/artifacts/$rt\_jar/$rt.jar sd0406@l040101-ws05.ua.pt:/home/sd0406/src

scp -r $st/out/artifacts/$st\_jar/$st.jar sd0406@l040101-ws06.ua.pt:/home/sd0406/src

scp -r $b/out/artifacts/$b\_jar/$b.jar sd0406@l040101-ws07.ua.pt:/home/sd0406/src

scp -r $hj/out/artifacts/$hj\_jar/$hj.jar sd0406@l040101-ws08.ua.pt:/home/sd0406/src

scp -r $s/out/artifacts/$s\_jar/$s.jar sd0406@l040101-ws09.ua.pt:/home/sd0406/src
