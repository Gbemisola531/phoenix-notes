package com.phoenix.phoenixnotes.ui.createNote

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.phoenix.phoenixnotes.R
import com.phoenix.phoenixnotes.data.model.Note
import com.phoenix.phoenixnotes.data.model.NoteColor
import com.phoenix.phoenixnotes.ui.theme.RichBlack

@ExperimentalUnsignedTypes
@ExperimentalAnimationApi
@Composable
fun CreateNote(navController: NavController, note: Note?) {
    val createNoteViewModel = hiltNavGraphViewModel<CreateNoteViewModel>()

    val stateViewModel = hiltNavGraphViewModel<CreateNoteStateViewModel>()

    val viewState by stateViewModel.createNoteState.collectAsState()
    val selectedColorState by stateViewModel.colorState.collectAsState()
    val noteState by stateViewModel.noteState.collectAsState()

    val context = LocalContext.current

    note?.let {
        var shouldUpdateColor by remember { mutableStateOf(true) }

        stateViewModel.onIdValueChange(it.id)
        stateViewModel.onTitleValueChange(it.title)
        stateViewModel.onContentValueChange(it.content)

        if (shouldUpdateColor) {
            stateViewModel.updateSelectedColor(NoteColor(Color(it.color.toULong())))
            shouldUpdateColor = false
        }
    }

    val selectedColor = selectedColorState.selectedColor.color

    val onSaveClicked = {
        when {
            noteState.isNoteValid -> {
                if (note == null)
                    createNoteViewModel.saveNote(stateViewModel.getNote())
                else createNoteViewModel.updateNote(stateViewModel.getNote())

                navController.popBackStack()
            }
            else -> Toast.makeText(context, "Your note is empty", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier
            .background(selectedColor)
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .padding(top = 2.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)
                .fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(selectedColor)
            )

            CreateNoteTopBar({
                navController.popBackStack()
            }, {}, {}, { onSaveClicked() }, selectedColorState.selectedColor)

            TextField(
                value = noteState.title,
                onValueChange = stateViewModel::onTitleValueChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = selectedColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Transparent),
                placeholder = { Text(text = "Title", fontSize = 17.sp) }
            )

            TextField(
                value = noteState.content,
                onValueChange = stateViewModel::onContentValueChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = selectedColor
                ),
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth(),
                placeholder = { Text(text = "Note", fontSize = 14.sp) })

            AddOptionContent(viewState.addOption, selectedColorState.selectedColor)

            NoteActions(
                showActions = viewState.showActions,
                noteColors = selectedColorState.noteColors,
                selectedColor = selectedColorState.selectedColor,
                onColorClicked = stateViewModel::updateSelectedColor,
                onDeleteClicked = {
                    note?.let {
                        createNoteViewModel.deleteNote(it)
                    }
                    navController.popBackStack()
                }
            )

            CreateNoteBottomBar(
                stateViewModel::onAddClicked,
                stateViewModel::onOptionsClicked,
                selectedColorState.selectedColor
            )
        }
    }
}

@Composable
fun CreateNoteTopBar(
    onBackClicked: () -> Unit,
    onPinClicked: () -> Unit,
    onReminderClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    selectedColor: NoteColor
) {
    Surface(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        ConstraintLayout(modifier = Modifier.background(selectedColor.color)) {
            val (backBtn, pinBtn, reminderBtn, saveBtn) = createRefs()

            IconButton(onBackClicked, modifier = Modifier
                .padding(4.dp)
                .constrainAs(backBtn) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_btn)
                )
            }

            IconButton(onPinClicked, modifier = Modifier
                .padding(1.dp)
                .constrainAs(pinBtn) {
                    end.linkTo(reminderBtn.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = stringResource(R.string.pin_btn)
                )
            }

            IconButton(onClick = onReminderClicked, modifier = Modifier
                .padding(1.dp)
                .constrainAs(reminderBtn) {
                    end.linkTo(saveBtn.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                Icon(
                    imageVector = Icons.Default.NotificationAdd,
                    contentDescription = stringResource(R.string.reminder_btn)
                )
            }

            IconButton(onClick = onSaveClicked, modifier = Modifier
                .padding(1.dp)
                .constrainAs(saveBtn) {
                    end.linkTo(parent.end, margin = 5.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(R.string.save_btn)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AddOptionContent(addOption: Boolean, selectedColor: NoteColor) {
    AnimatedVisibility(
        modifier = Modifier.background(selectedColor.color),
        visible = addOption,
        enter = slideInVertically(
            initialOffsetY = { 500 },
            animationSpec = tween(
                delayMillis = 200,
                durationMillis = 700,
                easing = LinearOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { 500 },
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                elevation = 1.dp, backgroundColor = Color.Transparent
            ) {}
            Surface(
                shape = RoundedCornerShape(1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(selectedColor.color)
                ) {
                    item {
                        AddOption(
                            onOptionClick = {},
                            icon = Icons.Outlined.Camera,
                            contentDescription = stringResource(R.string.take_photo_icon),
                            stringResource(R.string.take_a_photo), selectedColor
                        )
                    }

                    item {
                        AddOption(
                            onOptionClick = {},
                            icon = Icons.Outlined.Photo,
                            contentDescription = stringResource(R.string.add_image_icon),
                            stringResource(R.string.add_image), selectedColor
                        )
                    }

                    item {
                        AddOption(
                            onOptionClick = {},
                            icon = Icons.Outlined.Mic,
                            contentDescription = stringResource(R.string.recording_icon),
                            stringResource(R.string.recording), selectedColor
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddOption(
    onOptionClick: () -> Unit,
    icon: ImageVector, contentDescription: String,
    text: String, selectedColor: NoteColor
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(top = 5.dp)
            .clickable(onClick = onOptionClick)
            .background(selectedColor.color),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier.padding(start = 15.dp),
            imageVector = icon,
            contentDescription = contentDescription
        )

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(15.dp)
        )

        Text(text = text)
    }
}

@ExperimentalAnimationApi
@Composable
fun NoteActions(
    showActions: Boolean,
    noteColors: List<NoteColor>,
    selectedColor: NoteColor,
    onColorClicked: (NoteColor) -> Unit,
    onDeleteClicked: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier.background(selectedColor.color),
        visible = showActions,
        enter = slideInVertically(
            initialOffsetY = { 500 },
            animationSpec = tween(
                delayMillis = 200,
                durationMillis = 700,
                easing = LinearOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { 500 },
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                elevation = 1.dp, backgroundColor = Color.Transparent
            ) {}
            Surface(
                shape = RoundedCornerShape(1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(selectedColor.color)
            ) {
                Column(Modifier.background(selectedColor.color)) {
                    LazyColumn(
                        modifier = Modifier
                            .background(selectedColor.color)
                    ) {
                        item {
                            AddOption(
                                onOptionClick = onDeleteClicked,
                                icon = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.delete_icon),
                                stringResource(R.string.delete), selectedColor
                            )
                        }

                        item {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(13.dp)
                            )
                        }
                    }

                    LazyRow(
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier.background(selectedColor.color)
                    ) {
                        items(noteColors) { noteColor ->
                            ColorAction(noteColor, selectedColor, onColorClicked)
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(15.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ColorAction(
    noteColor: NoteColor,
    selectedColor: NoteColor,
    onColorClicked: (NoteColor) -> Unit
) {
    Box(
        Modifier
            .padding(start = 10.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(noteColor.color)
            .border(
                0.5.dp,
                if (noteColor == selectedColor) RichBlack else noteColor.color,
                CircleShape
            )
            .clickable(onClick = {
                onColorClicked(noteColor)
            })
    ) {
        if (noteColor == selectedColor) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.color_check_icon)
            )
        }
    }
}

@Composable
fun CreateNoteBottomBar(
    onAddClicked: () -> Unit,
    onOptionsClick: () -> Unit,
    selectedColor: NoteColor
) {
    Box(
        Modifier
            .height(30.dp)
            .fillMaxWidth()
            .background(selectedColor.color)
    ) {
        IconButton(
            onClick = onAddClicked,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AddBox,
                contentDescription = stringResource(R.string.add_options_button)
            )
        }

        Text(
            "Edited", modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 5.dp)
        )

        IconButton(
            onClick = onOptionsClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.add_options_button)
            )
        }
    }
}