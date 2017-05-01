(
  var store, lastQueuedState;

  API.mountDuplexOSC();

  s.boot();

  store = StateStore.getInstance();

  // define a simple synth
  SynthDef(\simple, {
    arg freq, amp;
    var out;
    out = SinOsc.ar(freq, 0, amp) * EnvGen.kr(Env.linen(0.001, 0.05, 1.0), doneAction: 2);
    Out.ar(0, [out, out]);
  }).add();

  // when state changes, this method will be called
  lastQueuedState = nil;
  store.subscribe({
    var queuedState = store.getState().simpleSound.queued;
    
    if (lastQueuedState != queuedState, {
      lastQueuedState = queuedState;    

      if (queuedState == true, {
        Synth(\simple, [freq: 440, amp: 0.4]);
        store.dispatch((
          type: "SIMPLE_SOUND_SCHEDULED"
        ));
      });
    });
  });
)
