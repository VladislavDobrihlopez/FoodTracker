package com.voitov.tracker_presentation.custom_food_screen

import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.utils.UiSideEffect
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_presentation.custom_food_screen.components.PhotoPicker
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun CustomFoodScreen(
    onNavigateUp: () -> Unit,
    snackBarState: SnackbarHostState,
    viewModel: CustomFoodViewModel = hiltViewModel()
) {
    val appConfig = LocalConfiguration.current
    val context = LocalContext.current
    var orientation by remember {
        mutableStateOf(appConfig.orientation)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onEvent(CustomFoodScreenEvent.OnPhotoPicked(uri))
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiSideEffect.NavigateUp -> {
                    onNavigateUp()
                }

                is UiSideEffect.ShowUpSnackBar -> {
                    snackBarState.showSnackbar(message = event.text.asString(context))
                }
            }
        }

        snapshotFlow {
            appConfig.orientation
        }
            .distinctUntilChanged()
            .collect {
                orientation = it
            }
    }

    OrientatedScreenContent(
        orientation = orientation,
        screenState = viewModel.screenState,
        onSaveRequestButtonClick = {
            viewModel.onEvent(CustomFoodScreenEvent.OnSaveButtonClick)
            viewModel.screenState.imageUri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        },
        onAddImageButtonClick = {
            imagePicker.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onProductNameChange = { newName ->
            viewModel.onEvent(CustomFoodScreenEvent.OnValueEnter.OnNameEnter(value = newName))
        },
        onProteinAmountChange = { newProteinAmount ->
            viewModel.onEvent(CustomFoodScreenEvent.OnValueEnter.OnProteinsRatioEnter(value = newProteinAmount))
        },
        onFatAmountChange = { newFatAmount ->
            viewModel.onEvent(CustomFoodScreenEvent.OnValueEnter.OnFatRatioEnter(value = newFatAmount))
        },
        onCarbohydratesAmountChange = { newCarbohydratesAmount ->
            viewModel.onEvent(CustomFoodScreenEvent.OnValueEnter.OnCarbRatioEnter(value = newCarbohydratesAmount))
        }
    )
}

@Composable
private fun OrientatedScreenContent(
    orientation: Int,
    screenState: CustomFoodScreenState,
    onAddImageButtonClick: () -> Unit,
    onSaveRequestButtonClick: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onProteinAmountChange: (String) -> Unit,
    onFatAmountChange: (String) -> Unit,
    onCarbohydratesAmountChange: (String) -> Unit
) {
    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PortraitScreenContent(
            screenState = screenState,
            onAddImageButtonClick = onAddImageButtonClick,
            onSaveRequestButtonClick = onSaveRequestButtonClick,
            onProductNameChange = onProductNameChange,
            onProteinAmountChange = onProteinAmountChange,
            onFatAmountChange = onFatAmountChange,
            onCarbohydratesAmountChange = onCarbohydratesAmountChange,
            isPhotoPickerShown = true
        )

        Configuration.ORIENTATION_LANDSCAPE -> LandscapeScreenContent(
            screenState = screenState,
            onAddImageButtonClick = onAddImageButtonClick,
            onSaveRequestButtonClick = onSaveRequestButtonClick,
            onProductNameChange = onProductNameChange,
            onProteinAmountChange = onProteinAmountChange,
            onFatAmountChange = onFatAmountChange,
            onCarbohydratesAmountChange = onCarbohydratesAmountChange
        )
    }
}

@Composable
private fun PortraitScreenContent(
    screenState: CustomFoodScreenState,
    isPhotoPickerShown: Boolean,
    onAddImageButtonClick: () -> Unit,
    onSaveRequestButtonClick: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onProteinAmountChange: (String) -> Unit,
    onFatAmountChange: (String) -> Unit,
    onCarbohydratesAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.spaceSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (isPhotoPickerShown) {
            PhotoPicker(
                photoUri = screenState.imageUri,
                onContentClick = onAddImageButtonClick
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
        }

        OutlinedTextField(
            value = screenState.enteredName,
            label = { Text(text = stringResource(id = R.string.enter_the_product_name)) },
            onValueChange = onProductNameChange,
            isError = screenState.isEnteredNameIncorrect,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            )
        )

        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))

        OutlinedTextField(
            value = screenState.enteredCarbsPer100g,
            label = { Text(text = stringResource(id = R.string.enter_the_amount_of_carbs_in_100g)) },
            onValueChange = onCarbohydratesAmountChange,
            isError = screenState.isEnteredCarbsAmountIncorrect,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))

        OutlinedTextField(
            value = screenState.enteredFatPer100g,
            label = { Text(text = stringResource(id = R.string.enter_the_amount_of_fat_in_100g)) },
            onValueChange = onFatAmountChange,
            isError = screenState.isEnteredFatAmountIncorrect,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))

        OutlinedTextField(
            value = screenState.enteredProteinPer100g,
            label = { Text(text = stringResource(id = R.string.enter_the_amount_of_protein_in_100g)) },
            onValueChange = onProteinAmountChange,
            isError = screenState.isEnteredProteinAmountIncorrect,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(spacing.spaceSmall))

        Button(onClick = onSaveRequestButtonClick) {
            Text(text = stringResource(id = R.string.save), style = MaterialTheme.typography.button)
        }
    }
}

@Composable
private fun LandscapeScreenContent(
    screenState: CustomFoodScreenState,
    onAddImageButtonClick: () -> Unit,
    onSaveRequestButtonClick: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onProteinAmountChange: (String) -> Unit,
    onFatAmountChange: (String) -> Unit,
    onCarbohydratesAmountChange: (String) -> Unit
) {
    val spacing = LocalSpacing.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhotoPicker(
                modifier = Modifier.fillMaxSize(),
                photoUri = screenState.imageUri,
                onContentClick = onAddImageButtonClick,
                iconMargin = spacing.spaceMedium
            )
        }
        Spacer(modifier = Modifier.width(spacing.spaceSmall))

        PortraitScreenContent(
            modifier = Modifier.weight(1f),
            screenState = screenState,
            onAddImageButtonClick = onAddImageButtonClick,
            onSaveRequestButtonClick = onSaveRequestButtonClick,
            onProductNameChange = onProductNameChange,
            onProteinAmountChange = onProteinAmountChange,
            onFatAmountChange = onFatAmountChange,
            onCarbohydratesAmountChange = onCarbohydratesAmountChange,
            isPhotoPickerShown = false
        )
    }
}
