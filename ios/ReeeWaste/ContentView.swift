//
//  ContentView.swift
//  ReeeWaste
//
//  Created by Kyros Chow on 2/23/20.
//  Copyright © 2020 Kyros Chow. All rights reserved.
//

import SwiftUI
import common

struct ContentView: View {
    var body: some View {
        Text(HelloKt.platformMessage())
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
