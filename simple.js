#!/usr/bin/env node

/**
 *  @file       simple.js
 *
 *	@desc       Arbitrary reducers being sent to SuperCollider, the
 *	            corresponding SuperCollider script will respond appropriately.
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *  @copyright  2017 Colin Sullivan
 *  @license    Licensed under the MIT license.
 **/

  

import { createStore } from "redux"
import supercolliderRedux from "supercollider-redux"
import SCStoreController from "./SCStoreController"

// When we want the sound to play
const SIMPLE_SOUND_QUEUED = "SIMPLE_SOUND_QUEUED";
// when the sound is actually scheduled to play in the sound engine
const SIMPLE_SOUND_SCHEDULED = "SIMPLE_SOUND_SCHEDULED"

var actions = {
  simpleSoundQueued: () => {
    return {
      type: SIMPLE_SOUND_QUEUED
    }
  }
};

var simpleSound = function (state = {queued: false}, action) {
  switch (action.type) {
    case SIMPLE_SOUND_QUEUED:
      state.queued = true;
      break;
    case SIMPLE_SOUND_SCHEDULED:
      state.queued = false;
      break;
    default:
      break;
  }
  return state;
};

var rootReducer = function (state = {}, action) {

  state.simpleSound = simpleSound(state.simpleSound, action);
  state.supercolliderRedux = supercolliderRedux.reducer(state.supercolliderRedux, action);

  return state;
  
};

var store = createStore(rootReducer);
var scStoreController = new SCStoreController(store);

setInterval(() => {
  console.log("Queueing simple sound...");
  store.dispatch(actions.simpleSoundQueued());
}, 1000);
