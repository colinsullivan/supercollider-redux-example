/**
 *  @file       abletonlink.sc
 *
 *	@desc       Respond to state changes from Ableton Link in SuperCollider.
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *  @copyright  2017 Colin Sullivan
 *  @license    Licensed under the MIT license.
 **/

(
  var store, lastBeatFloor;

  API.mountDuplexOSC();

  s.boot();

  store = StateStore.getInstance();

  // define a simple synth
  SynthDef(\simple, {
    arg freq, amp;
    var out;
    out = SinOsc.ar(freq, 0, amp) * EnvGen.kr(Env.linen(0.001, 0.05, 0.3), doneAction: 2);
    Out.ar(0, [out, out]);
  }).add();

  // when state changes, this method will be called
  lastBeatFloor = 0;
  store.subscribe({
    var state = store.getState();
    var beat = state.abletonlink.beat;
    var bpm = state.abletonlink.bpm;
    var secondsPerBeat;
    var beatFloor = beat.floor();
    var noteFreq;
    
    if (bpm == false, {
      ^this;    
    });
    secondsPerBeat = 60.0 / bpm;

    if (lastBeatFloor != beatFloor, {

      if (beatFloor % 4 == 0, {
        noteFreq = 880;
      }, {
        noteFreq = 440;
      });
      s.makeBundle(secondsPerBeat, {Synth(\simple, [freq: noteFreq, amp: 0.1]); });

      lastBeatFloor = beatFloor;    
    });



  });
)
