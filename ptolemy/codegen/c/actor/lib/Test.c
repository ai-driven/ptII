/***preinitBlock***/
// FIXME: how do we reset count?
static int $actorSymbol(count) = 0; 
/**/

/***fireBlock***/
    // Test
    // FIXME: Need to handle all channels in the multiport input.
    if ($actorSymbol(count) < $size(correctValues) && fabs($ref(input#0) - $ref(correctValues, $actorSymbol(count))) > $ref(tolerance)) {
        // FIXME: what about types other than double?
        printf("Test fails in iteration %d.\n Value was: %f. Should have been: %f\n",
            $actorSymbol(count), (double)$ref(input#0), (double)$ref(correctValues, $actorSymbol(count)));
    }
    $actorSymbol(count) ++;
/**/
