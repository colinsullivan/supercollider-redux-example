(
  var store, lastBeatFloor, clock, pat;

  API.mountDuplexOSC();

  s.boot();

  store = StateStore.getInstance();
  clock = false;

  // define a simple synth
  SynthDef(\simple, {
    arg freq, amp = 0.2;
    var out;
    out = SinOsc.ar(freq, 0, amp) * EnvGen.kr(Env.linen(0.001, 0.05, 0.3), doneAction: 2);
    Out.ar(0, [out, out]);
  }).add();

  pat = Pbind(
    // the name of the SynthDef to use for each note
    \instrument, \simple,
    // MIDI note numbers -- converted automatically to Hz
    \midinote, Pseq([60, 72, 71, 67, 69, 71, 72, 60, 69, 67], inf),
    //\midinote, Pseq([96, 84, 84, 84], inf),
    // rhythmic values
    \dur, Pseq([2, 2, 1, 0.5, 0.5, 1, 1, 2, 2, 4], inf)
    //\dur, Pseq([1], inf)
  );


  // when state changes, this method will be called
  lastBeatFloor = 0;
  store.subscribe({
    var state = store.getState();
    var beat = state.abletonlink.beat;
    var bpm = state.abletonlink.bpm;
    //var secondsPerBeat;
    var beatFloor = beat.floor();
    //var noteFreq;
    
    if (bpm == false, {
      ^this;    
    });

    if (clock == false, {
      "initializing TempoClock...".postln();
      clock = TempoClock.new(tempo: bpm / 60.0, beats: beat);
      "TempoClock initialized.".postln();
      "playing pattern...".postln();
      pat.play(clock: clock, quant: [4, 0]);
    }, {
      clock.beats = beat;
    });
    //secondsPerBeat = 60.0 / bpm;

    if (lastBeatFloor != beatFloor, {
      "beatFloor:".postln;
      beatFloor.postln;

      //if (beatFloor % 3 == 0, {
        //noteFreq = 880;
      //}, {
        //noteFreq = 440;
      //});
      //s.makeBundle(secondsPerBeat, {Synth(\simple, [freq: noteFreq, amp: 0.4]); });

      lastBeatFloor = beatFloor;    
    });



  });
)
